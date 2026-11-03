package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.checkout.domain.model.params.PaymentMethodSource
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.state.CardFormScreenState
import java.math.BigDecimal

internal fun CheckoutConfiguration?.toProcessOrderParams(
    screenState: CardFormScreenState,
    installment: Int,
    token: String,
    cardId: String?,
    installmentAmount: BigDecimal? = null,
): ProcessOrderParams =
    ProcessOrderParams(
        orderId = this.paymentBrickOrderId().orEmpty(),
        clientToken = this.paymentBrickClientToken().orEmpty(),
        paymentMethodId = screenState.paymentState.paymentMethodId.orEmpty(),
        paymentMethodType = screenState.paymentState.paymentTypeId.orEmpty(),
        token = token,
        installments = installment,
        checkoutType = this.toCheckoutType(),
        installmentAmount = installmentAmount?.toPlainString(),
        bin = screenState.cardNumberState.cardBin.orEmpty(),
        lastFourDigits = screenState.cardNumberState.lastFourDigits,
        issuerId = screenState.cardIssuers.firstOrNull()?.id,
        cardId = cardId,
        paymentMethodSource = PaymentMethodSource.NewCard,
    )

internal fun CheckoutConfiguration?.buildProcessOrderParamsForMethodSelection(
    option: MethodSelectionOption,
    paymentTypeId: String,
): ProcessOrderParams? {
    val order = (this?.checkoutType as? MPCheckoutType.Payment)?.order ?: return null
    return ProcessOrderParams(
        orderId = order.orderId,
        clientToken = order.clientToken,
        paymentMethodId = option.id,
        paymentMethodType = paymentTypeId,
        token = "",
        installments = 0,
        checkoutType = this.toCheckoutType(),
    )
}
