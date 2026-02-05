package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.data.mapper.toInstallmentsState
import com.mercadopago.sdk.android.checkout.presentation.event.InstallmentsScreenEvent
import com.mercadopago.sdk.android.checkout.presentation.extensions.getCurrencyString
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotal
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotalDecimalPart
import com.mercadopago.sdk.android.checkout.presentation.state.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

internal class InstallmentsViewModel(
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
) : ViewModel() {
    private val _viewState = MutableStateFlow(InstallmentsScreenState())
    val viewState: StateFlow<InstallmentsScreenState> = _viewState.asStateFlow()

    private val _viewEvent = MutableStateFlow<InstallmentsScreenEvent>(InstallmentsScreenEvent.Idle)
    val viewEvent: StateFlow<InstallmentsScreenEvent> = _viewEvent.asStateFlow()

    fun getInstallments(
        bin: String,
        amount: BigDecimal,
    ) {
        viewModelScope.launch {
            val result = coreMethods.getInstallments(bin = bin, amount = amount)
            when (result) {
                is Result.Success ->
                    _viewState.value = _viewState.value.copy(
                        title = "Escolha o parcelamento",
                        installmentsState = result.data.getOrNull(0).toInstallmentsState(),
                        footerState = FooterState(
                            title = "Total",
                            currencySymbol = null.getCurrencyString(),
                            amountIntegerPart = amount.getTotal(),
                            amountDecimalPart = amount.getTotalDecimalPart(),
                            subtitle = "Santander Credito **** 1234",
                        ),
                    )
                is Result.Error -> Unit
            }
        }
    }

    fun onInstallmentSelected(installment: Int) {
        _viewEvent.value = InstallmentsScreenEvent.OnInstallmentsSelected(
            installment = installment,
        )
    }
}
