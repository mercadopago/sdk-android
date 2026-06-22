package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.CVVScreenData

/**
 * Immutable UI state for the CVV entry screen.
 *
 * @property screenData BFF-supplied configuration (labels, placeholder, button label).
 *   Null while loading or on error.
 * @property cvvLength Current digit count in the CVV field — **not** the actual value.
 *   The CVV value stays inside `PCIFieldState` in `:core-methods` (PCI rule).
 * @property isContinueEnabled Whether the continue button should be enabled.
 * @property errorMessage Inline validation message to display below the field.
 *   Null when the field is valid or untouched.
 * @property isLoading Whether a network operation (tokenization/process) is in progress.
 */
internal data class CVVScreenState(
    val screenData: CVVScreenData? = null,
    val cvvLength: Int = 0,
    val isContinueEnabled: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)
