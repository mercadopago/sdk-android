package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.MPResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetCardIssuersParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult

internal class GetCardIssuersUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(
        bin: String,
        paymentMethodId: String,
    ): MPResult<List<CardIssuer>, MPResultError> {
        return repository.getCardIssuers(
            GetCardIssuersParams(
                bin = bin.toIntOrNull(),
                paymentMethodId = paymentMethodId,
            ),
        )
    }
}
