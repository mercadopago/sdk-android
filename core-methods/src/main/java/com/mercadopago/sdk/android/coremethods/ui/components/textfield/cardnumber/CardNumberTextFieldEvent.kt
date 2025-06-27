package com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber

/**
 * Sealed interface representing events triggered by the card number text field.
 * This interface defines various events that can occur during card number input,
 * providing real-time feedback about the input state and validation.
 * It is used to handle user interactions and update the UI accordingly.
 *
 * The events include bin changes, length changes, focus changes, validation status,
 * and last four digits completion, all of which help in providing a smooth
 * card number input experience.
 *
 * @see CardNumberTextField
 */
interface CardNumberTextFieldEvent {
    /**
     * Event triggered when the card BIN (first 8 digits) changes.
     * This event is fired whenever the user types or deletes digits in the BIN portion
     * of the card number, allowing for real-time card type detection.
     *
     * @param cardBin The first 8 digits of the card number, or null if deleted
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is CardNumberTextFieldEvent.OnBinChanged -> {
     *         event.cardBin?.let { bin ->
     *             // Fetch card details based on BIN
     *             fetchCardDetails(bin)
     *         }
     *     }
     * }
     * ```
     */
    data class OnBinChanged(val cardBin: String?) : CardNumberTextFieldEvent

    /**
     * Event triggered when the length of the card number input changes.
     * This event helps track the progress of card number input and can be used
     * to update UI elements like progress indicators.
     *
     * @param length The current number of digits entered
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is CardNumberTextFieldEvent.OnLengthChanged -> {
     *         updateProgressBar(event.length)
     *     }
     * }
     * ```
     */
    data class OnLengthChanged(val length: Int) : CardNumberTextFieldEvent

    /**
     * Event triggered when the focus state of the card number field changes.
     * This event helps manage the visual state of the input field and can be used
     * to show/hide additional UI elements based on focus.
     *
     * @param isFocused Whether the field currently has focus
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is CardNumberTextFieldEvent.OnFocusChanged -> {
     *         if (event.isFocused) {
     *             showCardTypeHint()
     *         } else {
     *             hideCardTypeHint()
     *         }
     *     }
     * }
     * ```
     */
    data class OnFocusChanged(val isFocused: Boolean) : CardNumberTextFieldEvent

    /**
     * Event triggered when the card number validation status changes.
     * This event indicates whether the entered card number passes the Luhn algorithm
     * validation, helping to determine if the input is valid.
     *
     * @param isValid Whether the card number is valid according to the Luhn algorithm
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is CardNumberTextFieldEvent.IsValid -> {
     *         if (event.isValid) {
     *             enableNextButton()
     *         } else {
     *             disableNextButton()
     *         }
     *     }
     * }
     * ```
     */
    data class IsValid(val isValid: Boolean) : CardNumberTextFieldEvent

    /**
     * Event triggered when the last four digits of a valid card number are entered.
     * This event is only fired when the card number is valid according to the Luhn
     * algorithm and the last four digits have been entered.
     *
     * @param lastFourDigits The last four digits of the valid card number
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is CardNumberTextFieldEvent.OnLastFourDigitsFilled -> {
     *         showCardPreview(event.lastFourDigits)
     *     }
     * }
     * ```
     */
    data class OnLastFourDigitsFilled(val lastFourDigits: String) : CardNumberTextFieldEvent
}
