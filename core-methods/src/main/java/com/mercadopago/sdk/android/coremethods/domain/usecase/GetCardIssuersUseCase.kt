package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetCardIssuersParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class GetCardIssuersUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(
        bin: Int,
        paymentMethodId: String,
    ): Result<List<CardIssuer>, ResultError> {
        return repository.getCardIssuers(
            GetCardIssuersParams(
                productId = "",
                bin = bin,
                paymentMethodId = paymentMethodId
            ),
        )
    }
}
