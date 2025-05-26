package com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * State holder for PCI-compliant input fields in the payment form.
 * This class manages the state of secure input fields that handle sensitive payment information,
 * ensuring compliance with Payment Card Industry (PCI) security standards.
 *
 * The state is preserved across configuration changes and process death using Compose's
 * state restoration system. It provides a secure way to handle sensitive payment data
 * while maintaining state consistency throughout the payment flow.
 *
 * Example:
 * ```kotlin
 * // Create a PCI field state
 * val state = rememberPCIFieldState()
 *
 * // Use the state in a PCI-compliant text field
 * CardNumberTextField(
 *     state = state,
 *     onEvent = { event ->
 *         // Handle events
 *     }
 * )
 * ```
 * @see rememberPCIFieldState
 */
@Stable
class PCIFieldState internal constructor() {
    internal var input: String by mutableStateOf("")

    /**
     * Companion object providing state restoration functionality.
     * This allows the PCI field state to be preserved across configuration changes
     * and process death using Compose's state restoration system.
     *
     * The saver handles:
     * - Saving the current input value
     * - Restoring the state with the saved input value
     * - Creating a new state instance if needed
     */
    companion object {
        internal val Saver: Saver<PCIFieldState, String> = Saver<PCIFieldState, String>(
            save = { it.input },
            restore = { restored ->
                PCIFieldState().apply {
                    input = restored
                }
            },
        )
    }
}

/**
 * Creates a new instance of PCIFieldState that persists across configuration changes.
 * This composable function should be used to create state holders for PCI-compliant
 * input fields in the payment form.
 *
 * The state is automatically preserved across:
 * - Configuration changes (e.g., screen rotation)
 * - Process death and recreation
 * - Navigation events
 *
 * Example:
 * ```kotlin
 * @Composable
 * fun PaymentForm() {
 *     val cardNumberState = rememberPCIFieldState()
 *     val expirationState = rememberPCIFieldState()
 *
 *     Column {
 *         CardNumberTextField(state = cardNumberState)
 *         ExpirationDateTextField(state = expirationState)
 *     }
 * }
 * ```
 *
 * @return A new PCIFieldState instance that will be preserved across configuration changes
 */
@Composable
fun rememberPCIFieldState(): PCIFieldState {
    return rememberSaveable<PCIFieldState>(saver = PCIFieldState.Saver) {
        PCIFieldState()
    }
}
