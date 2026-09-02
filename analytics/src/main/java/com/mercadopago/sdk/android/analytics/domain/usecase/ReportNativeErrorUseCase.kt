package com.mercadopago.sdk.android.analytics.domain.usecase

import com.mercadopago.sdk.android.analytics.domain.models.PendingNativeError
import com.mercadopago.sdk.android.analytics.domain.repository.NativeErrorRepository

internal class ReportNativeErrorUseCase(
    private val repository: NativeErrorRepository,
) {
    suspend operator fun invoke(error: PendingNativeError): Boolean = repository.report(error)
}
