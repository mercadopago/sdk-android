package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import java.math.BigDecimal

internal fun CheckoutConfiguration?.toProcessOrderParams(
    screenState: CardPaymentScreenState,
    installment: Int,
    token: String,
    amount: BigDecimal,
): ProcessOrderParams =
    ProcessOrderParams(
        orderId = this.getOrder()?.orderId.orEmpty(),
        clientToken = this.getOrder()?.clientToken.orEmpty(),
        paymentMethodId = screenState.paymentState.paymentMethodId.orEmpty(),
        paymentMethodType = screenState.paymentState.paymentTypeId.orEmpty(),
        token = token,
        installments = installment,
        amount = amount.toPlainString(),
        bin = screenState.cardNumberState.cardBin.orEmpty(),
        lastFourDigits = screenState.cardNumberState.lastFourDigits,
        issuerId = screenState.cardIssuers.firstOrNull()?.id,
    )

internal fun CheckoutConfiguration?.buildProcessOrderParamsForMethodSelection(
    option: MethodSelectionOption,
    amount: String,
): ProcessOrderParams? {
    val order = (this?.checkoutType as? MPCheckoutType.Payment)?.order ?: return null
    return ProcessOrderParams(
        orderId = order.orderId,
        clientToken = order.clientToken,
        paymentMethodId = option.id,
        paymentMethodType = "ticket",
        token = "",
        installments = 0,
        amount = amount,
    )
}
