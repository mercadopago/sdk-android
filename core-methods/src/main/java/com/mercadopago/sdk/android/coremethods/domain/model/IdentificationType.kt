package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents a type of identification document used for payment processing.
 * This class contains information about different identification document types
 * supported by the payment system, including their validation rules and display names.
 * Common examples include national ID cards, passports, and tax identification numbers.
 *
 * @param id The unique identifier for this identification type
 * @param name The display name of the identification type (e.g., "CPF", "DNI", "Passport")
 * @param type The category or classification of the identification type
 * @param minLength The minimum number of characters required for this identification type
 * @param maxLength The maximum number of characters allowed for this identification type
 *
 * Example:
 * ```kotlin
 * // Create an identification type for CPF (Brazilian tax ID)
 * val cpfType = IdentificationType(
 *     id = "CPF",
 *     name = "CPF",
 *     type = "number",
 *     minLength = 11,
 *     maxLength = 11
 * )
 * ```
 */
data class IdentificationType(
    val id: String? = null,
    val name: String? = null,
    val type: String? = null,
    val minLength: Int? = null,
    val maxLength: Int? = null,
)
