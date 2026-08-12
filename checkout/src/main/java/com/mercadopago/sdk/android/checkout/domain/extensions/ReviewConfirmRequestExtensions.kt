package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.domain.model.SellerInfoRequest
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams

internal fun ProcessOrderParams.buildReviewConfirmRequest(
    checkoutConfiguration: CheckoutConfiguration?,
): ReviewConfirmRequest {
    val sellerInfo = checkoutConfiguration?.screenConfigs
        ?.filterIsInstance<ScreenConfig.ReviewAndConfirm>()
        ?.firstOrNull()
        ?.seller

    return ReviewConfirmRequest(
        orderId = orderId,
        paymentMethodType = paymentMethodType,
        paymentMethodId = paymentMethodId,
        issuerId = null,
        bin = bin,
        productId = null,
        lastFourDigits = null,
        installments = installments,
        installmentAmount = amount,
        emailChangeEnabled = true,
        sellerInfo = sellerInfo?.let {
            SellerInfoRequest(
                name = it.name,
                iconUrl = it.logoUrl,
            )
        },
    )
}
