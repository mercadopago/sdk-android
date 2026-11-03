package com.mercadopago.sdk.android.checkout.domain.usecase

/**
 * Validates the security code (CVV) typed on the CVV screen.
 *
 * A code is valid only when it contains exclusively numeric digits and its length matches the
 * expected length for the selected card (3 or 4, coming from `SecurityCodeState.length`). Both
 * failure cases — empty and incomplete — result in the same behaviour upstream: showing
 * `screen.field.error`.
 */
internal class ValidateSecurityCodeUseCase {
    operator fun invoke(
        value: String,
        length: Int,
    ): Boolean = value.isNotEmpty() && value.all { it.isDigit() } && value.length == length
}
