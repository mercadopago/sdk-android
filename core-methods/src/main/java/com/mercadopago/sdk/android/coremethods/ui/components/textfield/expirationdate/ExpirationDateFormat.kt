package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_FOUR
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_SIX

/**
 * Represents different formats for card expiration date input.
 * This enum defines the supported formats for entering card expiration dates,
 * including both short (MM/YY) and long (MM/YYYY) formats.
 * It provides the necessary input length and mask for each format type.
 *
 * The enum is used to configure the expiration date input field's behavior,
 * ensuring consistent formatting and validation across the payment form.
 *
 * Example:
 * ```kotlin
 * // Use short format (MM/YY)
 * val shortFormat = ExpirationDateFormat.ShortFormat
 * val shortMask = shortFormat.mask // "##/##"
 * val shortDigits = shortFormat.digits // 4
 *
 * // Use long format (MM/YYYY)
 * val longFormat = ExpirationDateFormat.LongFormat
 * val longMask = longFormat.mask // "##/####"
 * val longDigits = longFormat.digits // 6
 *
 * // Apply format to input field
 * ExpirationDateTextField(
 *     state = state,
 *     onEvent = { /* handle events */ },
 *     dateFormat = ExpirationDateFormat.ShortFormat
 * )
 * ```
 *
 * @see ExpirationDateTextField
 *
 * @param digits The number of digits required for the input
 * @param mask The format mask to be applied to the input
 */
enum class ExpirationDateFormat(val digits: Int, val mask: String) {
    /**
     * Represents the short format for expiration dates (MM/YY).
     * This format requires 4 digits (2 for month, 2 for year)
     * and displays them in the format MM/YY.
     *
     * The format is commonly used for most payment cards and provides
     * a concise way to enter expiration dates.
     *
     * Example:
     * ```kotlin
     * // Use in text field
     * ExpirationDateTextField(
     *     state = state,
     *     onEvent = { /* handle events */ },
     *     dateFormat = ExpirationDateFormat.ShortFormat
     * )
     * ```
     */
    ShortFormat(INT_FOUR, "##/##"),

    /**
     * Represents the long format for expiration dates (MM/YYYY).
     * This format requires 6 digits (2 for month, 4 for year)
     * and displays them in the format MM/YYYY.
     *
     * The format is used when a full year representation is required,
     * providing more explicit date information.
     *
     * Example:
     * ```kotlin
     * // Use in text field
     * ExpirationDateTextField(
     *     state = state,
     *     onEvent = { /* handle events */ },
     *     dateFormat = ExpirationDateFormat.LongFormat
     * )
     * ```
     */
    LongFormat(INT_SIX, "##/####"),
}
