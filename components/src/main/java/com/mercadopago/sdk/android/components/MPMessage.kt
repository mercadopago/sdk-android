package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val MESSAGE_GROUP = "Message"

/**
 * Message Hierarchy enum class, used to determine the visual hierarchy of the message
 * This is used to change the typography weight
 */
enum class MPMessageHierarchy {
    /**
     * Quiet: Message with Regular typography
     */
    Quiet,

    /**
     * Loud: Message with Bold typography
     */
    Loud,
}

/**
 * Message Type enum class, used to determine the message type and color scheme
 * This is used to change the colors and icon
 */
enum class MPMessageType {
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

/**
 * Message Color Defaults data class
 * Stores color tokens for the message component
 */
data class MessageColorDefaults(
    val textColor: Color,
    val iconColor: Color,
)

/**
 * Message Spacing Defaults data class
 * Stores spacing tokens for the message component
 */
data class MessageSpacingDefaults(
    val horizontalPadding: androidx.compose.ui.unit.Dp,
    val verticalPadding: androidx.compose.ui.unit.Dp,
    val iconTextSpacing: androidx.compose.ui.unit.Dp,
    val iconSize: androidx.compose.ui.unit.Dp,
)

/**
 * Message Icon Defaults data class
 * Stores icon configuration for the message component
 */
data class MessageIconDefaults(
    val icon: ImageVector,
)

/**
 * Message Typography Defaults data class
 * Stores typography configuration for the message component
 */
data class MessageTypographyDefaults(
    val fontWeight: FontWeight,
)

/**
 * Message Defaults data class
 * Main class containing all default configurations for the message component
 */
data class MessageDefaults(
    val colors: MessageColorDefaults,
    val spacing: MessageSpacingDefaults,
    val icon: MessageIconDefaults,
    val typography: MessageTypographyDefaults,
)

/**
 * Gets the message defaults based on hierarchy and type
 * Uses tokens from MercadoPagoAndesTheme
 */
@Composable
private fun getMessageDefaults(
    hierarchy: MPMessageHierarchy,
    type: MPMessageType,
): MessageDefaults {
    val feedbackColors = when (type) {
        MPMessageType.Informative -> MercadoPagoAndesTheme.color.feedback.informative
        MPMessageType.Positive -> MercadoPagoAndesTheme.color.feedback.positive
        MPMessageType.Caution -> MercadoPagoAndesTheme.color.feedback.caution
        MPMessageType.Negative -> MercadoPagoAndesTheme.color.feedback.negative
    }
    val textColor = feedbackColors.textLoud
    val iconColor = feedbackColors.iconLoud
    val fontWeight = when (hierarchy) {
        MPMessageHierarchy.Quiet -> FontWeight.W400
        MPMessageHierarchy.Loud -> FontWeight.W600
    }
    val icon = when (type) {
        MPMessageType.Informative -> Icons.Filled.Info
        MPMessageType.Positive -> Icons.Filled.Check
        MPMessageType.Caution -> Icons.Filled.Warning
        MPMessageType.Negative -> Icons.Filled.Close
    }
    return MessageDefaults(
        colors = MessageColorDefaults(
            textColor = textColor,
            iconColor = iconColor,
        ),
        spacing = MessageSpacingDefaults(
            horizontalPadding = MercadoPagoAndesTheme.spacing.gap.xmicro,
            verticalPadding = MercadoPagoAndesTheme.spacing.gap.xnano,
            iconTextSpacing = MercadoPagoAndesTheme.spacing.gap.xnano,
            iconSize = 16.dp,
        ),
        icon = MessageIconDefaults(
            icon = icon,
        ),
        typography = MessageTypographyDefaults(
            fontWeight = fontWeight,
        ),
    )
}

/**
 * Message component - Displays feedback messages with different hierarchies and types
 * This component uses Andes design tokens for colors, typography, and spacing
 *
 * @param text: Message text to display
 * @param modifier: Component modifier
 * @param hierarchy: Visual hierarchy of the message (Quiet or Loud)
 * @param type: Message type (Informative, Positive, Caution, or Negative)
 */
@Composable
fun MPMessage(
    text: String,
    modifier: Modifier = Modifier,
    hierarchy: MPMessageHierarchy = MPMessageHierarchy.Quiet,
    type: MPMessageType = MPMessageType.Informative,
) {
    val defaults = getMessageDefaults(hierarchy = hierarchy, type = type)
    Row(
        modifier = modifier
            .padding(
                horizontal = defaults.spacing.horizontalPadding,
                vertical = defaults.spacing.verticalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = defaults.icon.icon,
            contentDescription = null,
            modifier = Modifier
                .size(defaults.spacing.iconSize)
                .background(
                    color = defaults.colors.iconColor,
                    shape = CircleShape,
                )
                .padding(3.dp),
            tint = Color.White,
        )
        Spacer(modifier = Modifier.size(defaults.spacing.iconTextSpacing))
        MPText(
            text = text,
            style = when (hierarchy) {
                MPMessageHierarchy.Quiet -> MercadoPagoAndesTheme.typography.body.bodySmallDefault
                MPMessageHierarchy.Loud -> MercadoPagoAndesTheme.typography.body.bodySmallEmphasis
            },
            color = defaults.colors.textColor,
        )
    }
}

@Preview(name = "Message Informative Quiet", group = MESSAGE_GROUP)
@Composable
internal fun MessageInformativeQuietPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Quiet,
                type = MPMessageType.Informative,
            )
        }
    }
}

@Preview(name = "Message Informative Loud", group = MESSAGE_GROUP)
@Composable
internal fun MessageInformativeLoudPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Loud,
                type = MPMessageType.Informative,
            )
        }
    }
}

@Preview(name = "Message Positive Quiet", group = MESSAGE_GROUP)
@Composable
internal fun MessagePositiveQuietPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Quiet,
                type = MPMessageType.Positive,
            )
        }
    }
}

@Preview(name = "Message Positive Loud", group = MESSAGE_GROUP)
@Composable
internal fun MessagePositiveLoudPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Loud,
                type = MPMessageType.Positive,
            )
        }
    }
}

@Preview(name = "Message Caution Quiet", group = MESSAGE_GROUP)
@Composable
internal fun MessageCautionQuietPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Quiet,
                type = MPMessageType.Caution,
            )
        }
    }
}

@Preview(name = "Message Caution Loud", group = MESSAGE_GROUP)
@Composable
internal fun MessageCautionLoudPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Loud,
                type = MPMessageType.Caution,
            )
        }
    }
}

@Preview(name = "Message Negative Quiet", group = MESSAGE_GROUP)
@Composable
internal fun MessageNegativeQuietPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Quiet,
                type = MPMessageType.Negative,
            )
        }
    }
}

@Preview(name = "Message Negative Loud", group = MESSAGE_GROUP)
@Composable
internal fun MessageNegativeLoudPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Loud,
                type = MPMessageType.Negative,
            )
        }
    }
}

@Preview(name = "Message All Variations", group = MESSAGE_GROUP)
@Composable
internal fun MessageAllVariationsPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Quiet,
                type = MPMessageType.Informative,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Quiet,
                type = MPMessageType.Positive,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Quiet,
                type = MPMessageType.Caution,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Quiet,
                type = MPMessageType.Negative,
            )
            Spacer(modifier = Modifier.size(16.dp))
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Loud,
                type = MPMessageType.Informative,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Loud,
                type = MPMessageType.Positive,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Loud,
                type = MPMessageType.Caution,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPMessage(
                text = "Helper text",
                hierarchy = MPMessageHierarchy.Loud,
                type = MPMessageType.Negative,
            )
        }
    }
}
