package com.mercadopago.sdk.android.checkout.statusscreen.domain.usecase

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.domain.extensions.withServiceRetry
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData
import com.mercadopago.sdk.android.checkout.statusscreen.domain.repository.StatusScreenRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class GetStatusScreenUseCase(
    private val repository: StatusScreenRepository,
) {
    suspend operator fun invoke(
        clientToken: String,
        orderId: String,
        lastFourDigits: String? = null,
        sellerInfo: MPSellerInfo? = null,
        checkoutType: String? = null,
    ): Result<StatusScreenViewData, ResponseError> =
        withServiceRetry {
            repository.getStatusScreen(
                clientToken = clientToken,
                orderId = orderId,
                lastFourDigits = lastFourDigits,
                sellerInfo = sellerInfo,
                checkoutType = checkoutType,
            )
        }
}
