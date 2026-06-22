package com.mercadopago.sdk.android.checkout.domain.usecase

/**
 * Validates a CVV entry by comparing the **length** of input against the expected length.
 *
 * The actual CVV value is never passed here — it stays inside `PCIFieldState` in `:core-methods`
 * following the project's PCI rule. Format validation (digits only) is enforced by `PCITextField`
 * at the UI layer.
 *
 * Mirrors the pattern of [com.mercadopago.sdk.android.coremethods.domain.usecase.validations.IsSecurityCodeValidUseCase].
 */
internal class ValidateCVUseCase {
    operator fun invoke(
        cvvLength: Int,
        expectedLength: Int,
    ): CVVValidationResult =
        when {
            cvvLength == 0 -> CVVValidationResult.Invalid.Empty
            cvvLength != expectedLength -> CVVValidationResult.Invalid.IncorrectLength
            else -> CVVValidationResult.Valid
        }
}
