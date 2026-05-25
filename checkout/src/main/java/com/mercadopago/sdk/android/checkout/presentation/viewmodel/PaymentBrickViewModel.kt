package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.CardFormUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.UserCancelledContext
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickFooterState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentOptionState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentSectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class PaymentBrickViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(mockState())
    val viewState: StateFlow<PaymentBrickScreenState> = _viewState

    fun onBackPressed() {
        CheckoutCallbackHolder.notify(
            MercadoPagoCheckoutResult.UserCancelled(
                UserCancelledContext.CardForm(CardFormUserCancelledContext(emptyList())),
            ),
        )
    }

    private fun mockState() =
        PaymentBrickScreenState(
            title = "Escolha como pagar",
            sections = listOf(
                PaymentSectionState(
                    title = "Mercado Pago",
                    options = listOf(
                        PaymentOptionState(
                            id = "mp_balance",
                            title = "Saldo em conta ou cartões salvos",
                            thumbnailUrl = "",
                        ),
                        PaymentOptionState(
                            id = "credit_line",
                            title = "Linha de Crédito",
                            thumbnailUrl = "",
                        ),
                    ),
                ),
                PaymentSectionState(
                    title = "Outros meios de pagamento",
                    options = listOf(
                        PaymentOptionState(
                            id = "pix",
                            title = "Pix",
                            thumbnailUrl = "",
                        ),
                        PaymentOptionState(
                            id = "boleto",
                            title = "Boleto",
                            thumbnailUrl = "",
                        ),
                        PaymentOptionState(
                            id = "new_card",
                            title = "Novo cartão",
                            description = "Crédito ou pré-pago",
                            thumbnailUrl = "",
                        ),
                    ),
                ),
            ),
            footerState = PaymentBrickFooterState(
                currencySymbol = "R$",
                amountInteger = "500",
                amountDecimal = "00",
                buttonLabel = "Continuar",
            ),
        )
}
