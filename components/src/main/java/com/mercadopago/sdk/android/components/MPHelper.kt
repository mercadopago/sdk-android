package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val HELPER_GROUP = "Helper"

/**
 * Helper Hierarchy enum class, used to determine the visual hierarchy of the message
 * This is used to change the typography weight
 */
enum class MPHelperHierarchy {
    /**
     * Quiet: Helper with Regular typography
     */
    Quiet,

    /**
     * Loud: Helper with Bold typography
     */
    Loud,
}

/**
 * Helper Type enum class, used to determine the message type and color scheme
 * This is used to change the colors and icon
 */
enum class MPHelperType {
    /**
     * Informative: Blue color scheme for informational messages
     */
    Informative,

    /**
     * Positive: Green color scheme for success messages
     */
    Positive,

    /**
     * Caution: Orange color scheme for warning messages
     */
    Caution,

    /**
     * Negative: Red color scheme for error messages
     */
    Negative,
}

internal data class HelperColorDefaults(
    val textColor: Color,
    val textColorLoud: Color,
)

internal data class HelperSpacingDefaults(
    val horizontalPadding: androidx.compose.ui.unit.Dp,
    val verticalPadding: androidx.compose.ui.unit.Dp,
    val iconTextSpacing: androidx.compose.ui.unit.Dp,
    val iconSize: androidx.compose.ui.unit.Dp,
)

internal data class HelperDefaults(
    val colors: HelperColorDefaults,
    val spacing: HelperSpacingDefaults,
)

@Composable
private fun getHelperDefaults(
    type: MPHelperType,
): HelperDefaults {
    val feedbackColors = when (type) {
        MPHelperType.Informative -> MercadoPagoAndesTheme.color.feedback.informative
        MPHelperType.Positive -> MercadoPagoAndesTheme.color.feedback.positive
        MPHelperType.Caution -> MercadoPagoAndesTheme.color.feedback.caution
        MPHelperType.Negative -> MercadoPagoAndesTheme.color.feedback.negative
    }

    val textColorLoud = feedbackColors.textLoud
    val textColor = MercadoPagoAndesTheme.color.text.primary

    return HelperDefaults(
        colors = HelperColorDefaults(
            textColor = textColor,
            textColorLoud = textColorLoud,
        ),
        spacing = HelperSpacingDefaults(
            horizontalPadding = MercadoPagoAndesTheme.spacing.gap.xmicro,
            verticalPadding = MercadoPagoAndesTheme.spacing.gap.xnano,
            iconTextSpacing = MercadoPagoAndesTheme.spacing.gap.xnano,
            iconSize = 16.dp,
        ),
    )
}

/**
 * Helper component - Displays feedback messages with different hierarchies and types
 * This component uses Andes design tokens for colors, typography, and spacing
 *
 * @param text: Helper text to display
 * @param modifier: Component modifier
 * @param showIcon: Component icon
 * @param hierarchy: Visual hierarchy of the message (Quiet or Loud)
 * @param type: Helper type (Informative, Positive, Caution, or Negative)
 */
@Composable
fun MPHelper(
    text: String,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
    hierarchy: MPHelperHierarchy = MPHelperHierarchy.Quiet,
    type: MPHelperType = MPHelperType.Informative,
) {
    val defaults = getHelperDefaults(type = type)
    Row(
        modifier = modifier
            .padding(
                horizontal = defaults.spacing.horizontalPadding,
                vertical = defaults.spacing.verticalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val badgeType = when (type) {
            MPHelperType.Informative -> BadgeType.Informative
            MPHelperType.Positive -> BadgeType.Positive
            MPHelperType.Caution -> BadgeType.Caution
            MPHelperType.Negative -> BadgeType.Negative
        }

        if (showIcon) {
            MPBadgeIcon(badgeType = badgeType)
            Spacer(modifier = Modifier.size(defaults.spacing.iconTextSpacing))
        }

        MPText(
            text = text,
            style = when (hierarchy) {
                MPHelperHierarchy.Quiet -> MercadoPagoAndesTheme.typography.body.default.small
                MPHelperHierarchy.Loud -> MercadoPagoAndesTheme.typography.body.emphasis.small
            },
            color = when (hierarchy) {
                MPHelperHierarchy.Quiet -> defaults.colors.textColor
                MPHelperHierarchy.Loud -> defaults.colors.textColorLoud
            },
        )
    }
}

@Preview(name = "Helper Informative Quiet", group = HELPER_GROUP)
@Composable
internal fun MessageInformativeQuietPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Quiet,
                type = MPHelperType.Informative,
            )
        }
    }
}

@Preview(name = "Helper Informative Loud", group = HELPER_GROUP)
@Composable
internal fun MessageInformativeLoudPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Loud,
                type = MPHelperType.Informative,
            )
        }
    }
}

@Preview(name = "Helper Positive Quiet", group = HELPER_GROUP)
@Composable
internal fun MessagePositiveQuietPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Quiet,
                type = MPHelperType.Positive,
            )
        }
    }
}

@Preview(name = "Helper Positive Loud", group = HELPER_GROUP)
@Composable
internal fun MessagePositiveLoudPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Loud,
                type = MPHelperType.Positive,
            )
        }
    }
}

@Preview(name = "Helper Caution Quiet", group = HELPER_GROUP)
@Composable
internal fun MessageCautionQuietPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Quiet,
                type = MPHelperType.Caution,
            )
        }
    }
}

@Preview(name = "Helper Caution Loud", group = HELPER_GROUP)
@Composable
internal fun MessageCautionLoudPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Loud,
                type = MPHelperType.Caution,
            )
        }
    }
}

@Preview(name = "Helper Negative Quiet", group = HELPER_GROUP)
@Composable
internal fun MessageNegativeQuietPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Quiet,
                type = MPHelperType.Negative,
            )
        }
    }
}

@Preview(name = "Helper Negative Loud", group = HELPER_GROUP)
@Composable
internal fun MessageNegativeLoudPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Loud,
                type = MPHelperType.Negative,
            )
        }
    }
}

@Preview(name = "Helper All Variations", group = HELPER_GROUP)
@Composable
internal fun MessageAllVariationsPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Quiet,
                type = MPHelperType.Informative,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Quiet,
                type = MPHelperType.Positive,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Quiet,
                type = MPHelperType.Caution,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Quiet,
                type = MPHelperType.Negative,
            )
            Spacer(modifier = Modifier.size(16.dp))
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Loud,
                type = MPHelperType.Informative,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Loud,
                type = MPHelperType.Positive,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Loud,
                type = MPHelperType.Caution,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPHelper(
                text = "Helper text",
                hierarchy = MPHelperHierarchy.Loud,
                type = MPHelperType.Negative,
            )
        }
    }
}
