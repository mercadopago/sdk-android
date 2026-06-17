package com.mercadopago.sdk.android.checkout.presentation.state

/**
 * Represents the UI state for the PaymentBrick screen.
 *
 * @property title Screen title displayed in the header.
 * @property sections Grouped payment options to display.
 * @property footerState Footer configuration with total amount and CTA button.
 * @property isLoading Whether the screen is loading data.
 * @property isError Whether an unrecoverable error occurred during initialization.
 */
data class PaymentBrickScreenState(
    val title: String = "",
    val sections: List<PaymentSectionState> = emptyList(),
    val footerState: PaymentBrickFooterState? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)

/**
 * Represents a group of payment options under a common section title.
 *
 * @property title Section header label.
 * @property options Payment options within this section.
 */
data class PaymentSectionState(
    val title: String,
    val options: List<PaymentOptionState>,
)

/**
 * Represents a single selectable payment option row.
 *
 * @property id Unique identifier for routing on selection.
 * @property title Option label shown as the row title.
 * @property thumbnailUrl Remote URL for the leading thumbnail image, provided by the BFF.
 * @property description Optional secondary text shown below the title.
 */
data class PaymentOptionState(
    val id: String,
    val title: String,
    val thumbnailUrl: String? = null,
    val description: String? = null,
)

/**
 * Represents the informational footer for the PaymentBrick screen.
 *
 * @property totalLabel Label for the total row — translated by the BFF.
 * @property totalAmount Pre-formatted total string provided by the BFF (e.g. "$ 188.000").
 *   Displayed as-is; the SDK does not reformat this value.
 */
data class PaymentBrickFooterState(
    val totalLabel: String,
    val totalAmount: String,
)
