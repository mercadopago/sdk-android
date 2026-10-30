package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams

internal const val ITEM_TYPE_PAYER_EMAIL = "payer_email"

internal sealed interface PaymentBrickViewEvent {
    data class OnOptionSelected(val optionId: String) : PaymentBrickViewEvent

    data class OnSecurityCodeRequired(val config: SecurityCodeScreenConfig) : PaymentBrickViewEvent

    data class OnPaymentReadyForReview(val params: ProcessOrderParams) : PaymentBrickViewEvent

    data class OnOrderProcessed(
        val payment: MPPaymentData,
        val params: ProcessOrderParams,
        val paymentProcessed: OrderProcessOutput.PaymentProcessed?,
    ) : PaymentBrickViewEvent

    data class OnOrderProcessFailed(
        val error: MercadoPagoCheckoutError,
        val statusDetail: String?,
        val params: ProcessOrderParams,
    ) : PaymentBrickViewEvent

    data class OnInstallmentsRequired(
        val installmentData: MPInstallmentData,
        val paymentData: MPPaymentData.Payment,
    ) : PaymentBrickViewEvent

    data object OnInstallmentsUnavailable : PaymentBrickViewEvent

    data class OnFailure(val error: MercadoPagoCheckoutError) : PaymentBrickViewEvent

    data object OnTokenizationError : PaymentBrickViewEvent

    data class OnUserCancelled(val context: MPUserCancelledContext.Payment) : PaymentBrickViewEvent

    data class OnOfflineMethodSelected(val screenData: MethodSelectionScreenData) : PaymentBrickViewEvent
}

internal sealed interface CardFormViewEvent {
    data class OnSuccess(
        val payment: MPPaymentData,
        val installment: MPInstallmentData,
    ) : CardFormViewEvent

    data class OnPaymentConfirmed(
        val payment: MPPaymentData,
        val params: ProcessOrderParams,
    ) : CardFormViewEvent

    data class OnOrderProcessed(
        val payment: MPPaymentData,
        val params: ProcessOrderParams,
        val paymentProcessed: OrderProcessOutput.PaymentProcessed?,
    ) : CardFormViewEvent

    data class OnOrderProcessFailed(
        val error: MercadoPagoCheckoutError,
        val statusDetail: String?,
        val params: ProcessOrderParams,
    ) : CardFormViewEvent

    data class OnFailure(val error: MercadoPagoCheckoutError) : CardFormViewEvent

    data object OnTokenizationError : CardFormViewEvent

    data class OnUserCancelled(val context: MPUserCancelledContext) : CardFormViewEvent

    data object OnReturnToPaymentSelector : CardFormViewEvent

    data class OnBackPressed(val context: MPUserCancelledContext) : CardFormViewEvent

    object OnReviewConfirmError : CardFormViewEvent
}

internal sealed interface SecurityCodeViewEvent {
    data class OnTokenSuccess(
        val cardId: String,
        val token: String,
    ) : SecurityCodeViewEvent

    data class OnUserCancelled(
        val context: MPUserCancelledContext.Payment,
    ) : SecurityCodeViewEvent

    data class OnTokenError(
        val error: MercadoPagoCheckoutError,
    ) : SecurityCodeViewEvent
}

internal sealed interface InstallmentViewEvent {
    data class OnSuccess(val installment: Int) : InstallmentViewEvent

    data class OnFailure(val error: MercadoPagoCheckoutError) : InstallmentViewEvent

    data class OnUserCancelled(val context: MPUserCancelledContext) : InstallmentViewEvent
}

internal sealed interface ReviewConfirmViewEvent {
    data class OnPaymentSuccess(val output: OrderProcessOutput) : ReviewConfirmViewEvent

    data class OnPaymentError(
        val error: MercadoPagoCheckoutError,
        val statusDetail: String? = null,
    ) : ReviewConfirmViewEvent

    data class OnLoadFailure(val error: MercadoPagoCheckoutError) : ReviewConfirmViewEvent

    data class OnModifyPaymentMethod(val itemType: String) : ReviewConfirmViewEvent

    object OnModifyEmail : ReviewConfirmViewEvent
}
