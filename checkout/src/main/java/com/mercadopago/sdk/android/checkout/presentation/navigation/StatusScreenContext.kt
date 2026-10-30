package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput

internal class StatusScreenContext(
    val orderId: String,
    val clientToken: String,
    val lastFourDigits: String?,
    val sellerInfo: MPSellerInfo?,
    val checkoutType: String?,
    val paymentProcessed: OrderProcessOutput.PaymentProcessed? = null,
    val orderStatus: String? = null,
)
