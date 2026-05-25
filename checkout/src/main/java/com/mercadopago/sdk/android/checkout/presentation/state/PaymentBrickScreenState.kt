package com.mercadopago.sdk.android.checkout.presentation.state

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents the UI state for the PaymentBrick screen.
 *
 * @property title Screen title displayed in the header.
 * @property sections Grouped payment options to display.
 * @property footerState Footer configuration with total amount and CTA button.
 * @property isLoading Whether the screen is loading data.
 */
data class PaymentBrickScreenState(
    val title: String = "",
    val sections: List<PaymentSectionState> = emptyList(),
    val footerState: PaymentBrickFooterState? = null,
    val isLoading: Boolean = false,
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
 * @property thumbnailUrl Remote URL for the leading thumbnail image.
 * @property thumbnailIcon Local icon for the leading thumbnail image.
 * @property description Optional secondary text shown below the title.
 */
data class PaymentOptionState(
    val id: String,
    val title: String,
    val thumbnailUrl: String? = null,
    val thumbnailIcon: ImageVector? = null,
    val description: String? = null,
)

/**
 * Represents the fixed footer configuration for the PaymentBrick screen.
 *
 * @property currencySymbol Currency symbol (e.g. "R$").
 * @property amountInteger Integer part of the total amount.
 * @property amountDecimal Decimal part of the total amount.
 * @property buttonLabel CTA button label text.
 */
data class PaymentBrickFooterState(
    val currencySymbol: String,
    val amountInteger: String,
    val amountDecimal: String,
    val buttonLabel: String,
)
