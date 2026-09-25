package com.mercadopago.sdk.android.analytics.observability.domain.usecase

import com.mercadopago.sdk.android.analytics.observability.domain.models.PendingNativeError
import com.mercadopago.sdk.android.analytics.observability.domain.repository.NativeErrorRepository

internal class ReportNativeErrorUseCase(
    private val repository: NativeErrorRepository,
) {
    suspend operator fun invoke(error: PendingNativeError): Boolean = repository.report(error)
}
