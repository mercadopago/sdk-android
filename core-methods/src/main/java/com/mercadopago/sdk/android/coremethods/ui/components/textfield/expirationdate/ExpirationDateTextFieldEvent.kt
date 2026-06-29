package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

/**
 * Sealed interface representing events triggered by the expiration date text field.
 * This interface defines various events that can occur during expiration date input,
 * providing real-time feedback about the input state and validation.
 *
 * The events include focus changes, input completion, validation status, and length changes,
 * all of which help in providing a smooth expiration date input experience.
 *
 * Example:
 * ```kotlin
 * // Handle expiration date events
 * when (event) {
 *     is ExpirationDateTextFieldEvent.OnFocusChanged -> {
 *         if (event.isFocused) {
 *             // Show date format hint
 *         }
 *     }
 *     is ExpirationDateTextFieldEvent.IsValid -> {
 *         if (event.isValid) {
 *             // Enable next step
 *         }
 *     }
 *     is ExpirationDateTextFieldEvent.OnInputFilled -> {
 *         if (event.isFilled) {
 *             // Handle complete input
 *         }
 *     }
 * }
 * ```
 *
 * @see ExpirationDateTextField
 */
interface ExpirationDateTextFieldEvent {
    /**
     * Event triggered when the focus state of the expiration date field changes.
     * This event helps manage the visual state of the input field and can be used
     * to show/hide additional UI elements based on focus.
     *
     * @param isFocused Whether the field currently has focus
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is ExpirationDateTextFieldEvent.OnFocusChanged -> {
     *         if (event.isFocused) {
     *             showDateFormatHint()
     *         } else {
     *             hideDateFormatHint()
     *         }
     *     }
     * }
     * ```
     */
    data class OnFocusChanged(val isFocused: Boolean) : ExpirationDateTextFieldEvent

    /**
     * Event triggered when the expiration date input is completely filled.
     * This event indicates whether all required digits for the selected date format
     * have been entered.
     *
     * @param isFilled Whether all required digits have been entered
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is ExpirationDateTextFieldEvent.OnInputFilled -> {
     *         if (event.isFilled) {
     *             validateExpirationDate()
     *         }
     *     }
     * }
     * ```
     */
    data class OnInputFilled(val isFilled: Boolean) : ExpirationDateTextFieldEvent

    /**
     * Event triggered when the expiration date validation status changes.
     * This event indicates whether the entered date is valid according to the validation rules,
     * including checks for past dates and valid month values.
     *
     * @param isValid Whether the expiration date is valid
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is ExpirationDateTextFieldEvent.IsValid -> {
     *         if (event.isValid) {
     *             enableNextButton()
     *         } else {
     *             showValidationError()
     *         }
     *     }
     * }
     * ```
     */
    data class IsValid(val isValid: Boolean) : ExpirationDateTextFieldEvent

    /**
     * Event triggered when the length of the expiration date input changes.
     * This event helps track the progress of date input and can be used
     * to update UI elements like progress indicators.
     *
     * @param length The current number of digits entered
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is ExpirationDateTextFieldEvent.OnLengthChanged -> {
     *         updateProgressBar(event.length)
     *     }
     * }
     * ```
     */
    data class OnLengthChanged(val length: Int) : ExpirationDateTextFieldEvent
}
