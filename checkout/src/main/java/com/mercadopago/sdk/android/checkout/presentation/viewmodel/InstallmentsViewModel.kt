package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.getAmount
import com.mercadopago.sdk.android.checkout.core.model.internal.toInstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.data.mapper.toInstallmentsState
import com.mercadopago.sdk.android.checkout.presentation.event.InstallmentsScreenEvent
import com.mercadopago.sdk.android.checkout.presentation.extensions.getCurrencyString
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotal
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotalDecimalPart
import com.mercadopago.sdk.android.checkout.presentation.state.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

internal class InstallmentsViewModel(
    private val checkoutConfiguration: CheckoutConfiguration?,
) : ViewModel() {
    private val _viewState = MutableStateFlow(InstallmentsScreenState())
    val viewState: StateFlow<InstallmentsScreenState> = _viewState.asStateFlow()

    private val _viewEvent = MutableStateFlow<InstallmentsScreenEvent>(InstallmentsScreenEvent.Idle)
    val viewEvent: StateFlow<InstallmentsScreenEvent> = _viewEvent.asStateFlow()

    private val amount: BigDecimal = checkoutConfiguration?.getAmount() ?: BigDecimal.ZERO
    private val displayType: InstallmentsDisplayType = checkoutConfiguration.toInstallmentsDisplayType()

    private var lastFourDigits: String = ""
    private var paymentMethodId: String = ""

    fun setup(
        payerCosts: List<PayerCost>,
        lastFourDigits: String,
        paymentMethodId: String,
    ) {
        this.lastFourDigits = lastFourDigits
        this.paymentMethodId = paymentMethodId
        val installments = payerCosts.toInstallmentsState()
        _viewState.value = _viewState.value.copy(
            title = "Escolha o parcelamento",
            displayType = displayType,
            installmentsState = if (displayType == InstallmentsDisplayType.RadioButton) {
                installments.mapIndexed { index, state -> state.copy(isSelected = index == 0) }
            } else {
                installments
            },
            footerState = FooterState(
                title = "Total",
                currencySymbol = null.getCurrencyString(),
                amountIntegerPart = amount.getTotal(),
                amountDecimalPart = amount.getTotalDecimalPart(),
                subtitle = buildSubtitle(),
                buttonLabel = if (displayType == InstallmentsDisplayType.RadioButton) "Pagar" else null,
            ),
        )
    }

    fun onInstallmentSelected(
        installment: Int,
    ) {
        when (displayType) {
            InstallmentsDisplayType.Chevron ->
                _viewEvent.value = InstallmentsScreenEvent.OnInstallmentsSelected(installment)
            InstallmentsDisplayType.RadioButton ->
                _viewState.value = _viewState.value.copy(
                    installmentsState = _viewState.value.installmentsState.map {
                        it.copy(isSelected = it.number == installment)
                    },
                )
        }
    }

    fun onPayClicked() {
        val selected = _viewState.value.installmentsState.firstOrNull { it.isSelected } ?: return
        _viewEvent.value = InstallmentsScreenEvent.ConfirmPayment(selected.number)
    }

    private fun buildSubtitle(): String {
        val brand = paymentMethodId.replaceFirstChar { it.uppercaseChar() }
        return if (lastFourDigits.isNotEmpty()) "$brand **** $lastFourDigits" else brand
    }
}
