package com.mercadopago.sdk.android.analytics.observability.domain.interactor

import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorOperation

internal data class NativeErrorReceipt(
    val eventId: String,
    val shouldSendMelidata: Boolean,
)

internal interface NativeErrorReporting {
    fun capture(operation: NativeErrorOperation, input: NativeErrorInput): NativeErrorReceipt
    fun close()
}
