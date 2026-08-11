package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.exception.ExceptionFactory.mapError
import com.mercadopago.sdk.android.checkout.domain.extensions.withServiceRetry
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.checkout.domain.repository.ReviewConfirmRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class FetchReviewConfirmUseCase(
    private val repository: ReviewConfirmRepository,
) {
    suspend operator fun invoke(
        request: ReviewConfirmRequest,
    ): Result<ReviewConfirmViewData, MercadoPagoCheckoutError> =
        withServiceRetry {
            repository.fetchReviewConfirm(request = request)
        }.mapError(ErrorLocalized.REVIEW_CONFIRM)
}
