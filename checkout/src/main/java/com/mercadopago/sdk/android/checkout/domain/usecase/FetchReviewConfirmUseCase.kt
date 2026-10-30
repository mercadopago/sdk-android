package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ExceptionFactory.mapError
import com.mercadopago.sdk.android.checkout.domain.extensions.withServiceRetry
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.repository.ReviewConfirmRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class FetchReviewConfirmUseCase(
    private val repository: ReviewConfirmRepository,
) {
    suspend operator fun invoke(
        processOrderParams: ProcessOrderParams,
        emailChangeEnabled: Boolean = false,
        sellerInfo: MPSellerInfo? = null,
    ): Result<ReviewConfirmViewData, MercadoPagoCheckoutError> =
        withServiceRetry {
            repository.fetchReviewConfirm(
                processOrderParams = processOrderParams,
                emailChangeEnabled = emailChangeEnabled,
                sellerInfo = sellerInfo,
            )
        }.mapError(ErrorLocalized.REVIEW_CONFIRM)
}
