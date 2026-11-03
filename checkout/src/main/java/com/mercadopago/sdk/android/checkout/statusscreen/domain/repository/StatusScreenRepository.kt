package com.mercadopago.sdk.android.checkout.statusscreen.domain.repository

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface StatusScreenRepository {
    suspend fun getStatusScreen(
        clientToken: String,
        orderId: String,
        lastFourDigits: String?,
        sellerInfo: MPSellerInfo?,
        checkoutType: String? = null,
    ): Result<StatusScreenViewData, ResponseError>
}
