package com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode

/**
 * Sealed interface representing events triggered by the security code text field.
 * This interface defines various events that can occur during security code (CVV) input,
 * providing real-time feedback about the input state and validation.
 *
 * The events include focus changes, input completion, and length changes,
 * all of which help in providing a smooth security code input experience.
 *
 * Example:
 * ```kotlin
 * // Handle security code events
 * when (event) {
 *     is SecurityCodeTextFieldEvent.OnFocusChanged -> {
 *         if (event.isFocused) {
 *             // Show security code hint
 *         }
 *     }
 *     is SecurityCodeTextFieldEvent.OnInputFilled -> {
 *         if (event.isFilled) {
 *             // Handle complete input
 *         }
 *     }
 *     is SecurityCodeTextFieldEvent.OnLengthChanged -> {
 *         // Update progress indicator
 *     }
 * }
 * ```
 *
 * @see SecurityCodeTextField
 */
interface SecurityCodeTextFieldEvent {
    /**
     * Event triggered when the security code input is completely filled.
     * This event indicates whether all required digits for the security code
     * have been entered.
     *
     * @param isFilled Whether all required digits have been entered
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is SecurityCodeTextFieldEvent.OnInputFilled -> {
     *         if (event.isFilled) {
     *             enableNextButton()
     *         }
     *     }
     * }
     * ```
     */
    data class OnInputFilled(val isFilled: Boolean) : SecurityCodeTextFieldEvent

    /**
     * Event triggered when the length of the security code input changes.
     * This event helps track the progress of security code input and can be used
     * to update UI elements like progress indicators.
     *
     * @param length The current number of digits entered
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is SecurityCodeTextFieldEvent.OnLengthChanged -> {
     *         updateProgressBar(event.length)
     *     }
     * }
     * ```
     */
    data class OnLengthChanged(val length: Int) : SecurityCodeTextFieldEvent

    /**
     * Event triggered when the focus state of the security code field changes.
     * This event helps manage the visual state of the input field and can be used
     * to show/hide additional UI elements based on focus.
     *
     * @param isFocused Whether the field currently has focus
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is SecurityCodeTextFieldEvent.OnFocusChanged -> {
     *         if (event.isFocused) {
     *             showSecurityCodeHint()
     *         } else {
     *             hideSecurityCodeHint()
     *         }
     * }
     * ```
     */
    data class OnFocusChanged(val isFocused: Boolean) : SecurityCodeTextFieldEvent
}
