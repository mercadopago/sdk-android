package com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * State holder for PCI-compliant input fields in the payment form.
 * This class manages the state of secure input fields that handle sensitive payment information,
 * ensuring compliance with Payment Card Industry (PCI) security standards.
 *
 * **Security note**: PCI field data (card number, CVV, expiration date) is intentionally NOT
 * persisted to the Android Bundle or disk. This means data will be lost on process death.
 * This is the expected behavior to comply with PCI DSS — sensitive card data must never be
 * written to persistent storage.
 *
 * To preserve PCI field state across configuration changes (e.g., screen rotation),
 * host the [PCIFieldState] instances in your ViewModel:
 *
 * ```kotlin
 * class PaymentViewModel : ViewModel() {
 *     val cardNumberState = PCIFieldState.create()
 *     val expirationDateState = PCIFieldState.create()
 *     val securityCodeState = PCIFieldState.create()
 * }
 * ```
 *
 * @see rememberPCIFieldState
 */
@Stable
class PCIFieldState internal constructor() {
    internal var input: String by mutableStateOf("")

    /**
     * Returns true if the input field is empty, false otherwise.
     * This property can be used to determine if placeholder text should be displayed.
     */
    val isEmpty: Boolean
        get() = input.isEmpty()

    /** Factory methods for creating [PCIFieldState] instances outside of a Composable context. */
    companion object {
        /**
         * Creates a new [PCIFieldState] instance.
         * Use this factory method when you need to create PCI field states outside of a Composable,
         * for example in a ViewModel to survive configuration changes.
         *
         * ```kotlin
         * class PaymentViewModel : ViewModel() {
         *     val cardNumberState = PCIFieldState.create()
         * }
         * ```
         */
        fun create(): PCIFieldState = PCIFieldState()
    }
}

/**
 * Creates a new instance of [PCIFieldState] that is remembered across recompositions.
 *
 * **Important**: This state is NOT preserved across process death or configuration changes
 * (e.g., screen rotation). This is intentional for PCI DSS compliance — sensitive card data
 * (card number, CVV, expiration date) must never be serialized to Bundle or disk.
 *
 * To preserve state across configuration changes, host [PCIFieldState] in your ViewModel instead:
 *
 * ```kotlin
 * class PaymentViewModel : ViewModel() {
 *     val cardNumberState = PCIFieldState.create()
 *     val expirationDateState = PCIFieldState.create()
 *     val securityCodeState = PCIFieldState.create()
 * }
 *
 * @Composable
 * fun PaymentForm(viewModel: PaymentViewModel) {
 *     CardNumberTextField(state = viewModel.cardNumberState)
 *     ExpirationDateTextField(state = viewModel.expirationDateState)
 * }
 * ```
 *
 * @return A new [PCIFieldState] instance remembered for the current composition
 */
@Composable
fun rememberPCIFieldState(): PCIFieldState {
    return remember { PCIFieldState() }
}
