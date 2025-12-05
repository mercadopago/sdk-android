package com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield

/**
 * Sealed interface representing events triggered by the simple text field.
 * This interface defines basic events that can occur during text input,
 * providing real-time feedback about the input state.
 *
 * The events include value changes and focus changes, providing a generic
 * text input experience for any use case.
 *
 * Example:
 * ```kotlin
 * // Handle simple text field events
 * when (event) {
 *     is SimpleTextFieldEvent.OnValueChanged -> {
 *         // Handle value change
 *         println("New value: ${event.value}")
 *     }
 *     is SimpleTextFieldEvent.OnFocusChanged -> {
 *         if (event.isFocused) {
 *             // Show hint or perform action on focus
 *         }
 *     }
 * }
 * ```
 *
 * @see SimpleTextField
 */
interface SimpleTextFieldEvent {
    /**
     * Event triggered when the text field value changes.
     * This event is fired whenever the user types or deletes characters,
     * providing the current input value.
     *
     * @param value The current text value of the field
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is SimpleTextFieldEvent.OnValueChanged -> {
     *         updateState(event.value)
     *     }
     * }
     * ```
     */
    data class OnValueChanged(val value: String) : SimpleTextFieldEvent

    /**
     * Event triggered when the focus state of the text field changes.
     * This event helps manage the visual state of the input field and can be used
     * to show/hide additional UI elements based on focus.
     *
     * @param isFocused Whether the field currently has focus
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is SimpleTextFieldEvent.OnFocusChanged -> {
     *         if (event.isFocused) {
     *             showHint()
     *         } else {
     *             hideHint()
     *         }
     *     }
     * }
     * ```
     */
    data class OnFocusChanged(val isFocused: Boolean) : SimpleTextFieldEvent
}
