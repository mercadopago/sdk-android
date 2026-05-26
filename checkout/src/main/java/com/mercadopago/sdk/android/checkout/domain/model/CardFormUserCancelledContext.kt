package com.mercadopago.sdk.android.checkout.domain.model

/**
 * Contains detailed information about the card form state when the user cancelled.
 *
 * This data class provides a snapshot of all form fields at the moment the user
 * abandoned the checkout flow, allowing integrators to understand user behavior
 * and form completion patterns.
 *
 * @property fields List of field states showing which fields were filled, empty,
 * incomplete, or invalid when the form was cancelled
 * @property installmentsWasPresented Whether the installments screen was presented
 * before the user cancelled
 */
data class CardFormUserCancelledContext(
    val fields: List<CancelledFieldState>,
    val installmentsWasPresented: Boolean = false,
)
