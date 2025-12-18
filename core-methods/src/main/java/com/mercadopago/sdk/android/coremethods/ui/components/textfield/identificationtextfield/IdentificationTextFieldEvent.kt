package com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield

import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType

/**
 * Sealed interface representing events triggered by the identification text field.
 * This interface defines events that can occur during identification input,
 * providing real-time feedback about the input state and type selection.
 *
 * The events include value changes, focus changes, and identification type selection,
 * all of which help in providing a smooth identification input experience.
 *
 * Example:
 * ```kotlin
 * // Handle identification text field events
 * when (event) {
 *     is IdentificationTextFieldEvent.OnValueChanged -> {
 *         // Handle value change
 *         println("New value: ${event.value}")
 *     }
 *     is IdentificationTextFieldEvent.OnFocusChanged -> {
 *         if (event.isFocused) {
 *             // Show hint or perform action on focus
 *         }
 *     }
 *     is IdentificationTextFieldEvent.OnTypeSelected -> {
 *         // Handle identification type selection
 *         println("Selected type: ${event.identificationType.name}")
 *     }
 * }
 * ```
 *
 * @see IdentificationTextField
 */
interface IdentificationTextFieldEvent {
    /**
     * Event triggered when the identification value changes.
     * This event is fired whenever the user types or deletes characters,
     * providing the current input value.
     *
     * @param value The current text value of the identification field
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is IdentificationTextFieldEvent.OnValueChanged -> {
     *         updateIdentificationValue(event.value)
     *     }
     * }
     * ```
     */
    data class OnValueChanged(val value: String) : IdentificationTextFieldEvent

    /**
     * Event triggered when the focus state of the identification field changes.
     * This event helps manage the visual state of the input field and can be used
     * to show/hide additional UI elements based on focus.
     *
     * @param isFocused Whether the field currently has focus
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is IdentificationTextFieldEvent.OnFocusChanged -> {
     *         if (event.isFocused) {
     *             showIdentificationHint()
     *         } else {
     *             hideIdentificationHint()
     *         }
     *     }
     * }
     * ```
     */
    data class OnFocusChanged(val isFocused: Boolean) : IdentificationTextFieldEvent

    /**
     * Event triggered when an identification type is selected from the dropdown.
     * This event provides the selected identification type including its validation rules.
     *
     * @param identificationType The selected identification type with its properties
     *
     * Example:
     * ```kotlin
     * when (event) {
     *     is IdentificationTextFieldEvent.OnTypeSelected -> {
     *         updateSelectedType(event.identificationType)
     *         // Clear current value when type changes
     *         clearIdentificationValue()
     *     }
     * }
     * ```
     */
    data class OnTypeSelected(val identificationType: IdentificationType) : IdentificationTextFieldEvent
}
