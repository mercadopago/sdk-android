package com.mercadopago.sdk.android.analytics.domain.usecase

import com.mercadopago.sdk.android.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.firstOrNull

internal class GetSessionIdUseCase(
    private val analyticsRepository: AnalyticsRepository,
) {

    suspend operator fun invoke(): String {
        return analyticsRepository.getCurrentSessionId().firstOrNull().orEmpty()
    }
}
