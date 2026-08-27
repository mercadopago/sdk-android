package com.mercadopago.sdk.android.checkout.presentation.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal enum class PaymentFeedback {
    GenericError,
}

internal data class PaymentFeedbackEvent(
    val id: Long,
    val feedback: PaymentFeedback,
)

internal class PaymentFeedbackStore {
    private var eventId = 0L
    private val _event = MutableStateFlow<PaymentFeedbackEvent?>(null)
    val event: StateFlow<PaymentFeedbackEvent?> = _event.asStateFlow()

    fun show(
        feedback: PaymentFeedback,
    ) {
        _event.value = PaymentFeedbackEvent(
            id = ++eventId,
            feedback = feedback,
        )
    }

    fun consume(
        event: PaymentFeedbackEvent,
    ) {
        _event.update { currentEvent ->
            currentEvent.takeUnless { it == event }
        }
    }

    fun clear() {
        _event.value = null
    }
}
