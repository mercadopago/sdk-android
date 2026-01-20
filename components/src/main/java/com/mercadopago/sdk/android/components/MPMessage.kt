package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val MESSAGE_GROUP = "Message"

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

internal data class MessageColorDefaults(
    val backgroundColor: Color,
    val textColor: Color,
    val closeIconColor: Color,
)

internal data class MessageSpacingDefaults(
    val horizontalPadding: androidx.compose.ui.unit.Dp,
    val verticalPadding: androidx.compose.ui.unit.Dp,
    val iconTextSpacing: androidx.compose.ui.unit.Dp,
    val closeIconSize: androidx.compose.ui.unit.Dp,
    val closeIconPadding: androidx.compose.ui.unit.Dp,
)

internal data class MessageDefaults(
    val colors: MessageColorDefaults,
    val spacing: MessageSpacingDefaults,
)

@Composable
private fun getMessageDefaults(
    type: MPMessageType,
): MessageDefaults {
    val feedbackColors = when (type) {
        MPMessageType.Informative -> MercadoPagoAndesTheme.color.feedback.informative
        MPMessageType.Positive -> MercadoPagoAndesTheme.color.feedback.positive
        MPMessageType.Caution -> MercadoPagoAndesTheme.color.feedback.caution
        MPMessageType.Negative -> MercadoPagoAndesTheme.color.feedback.negative
    }

    val backgroundColor = feedbackColors.fillQuiet
    val textColor = MercadoPagoAndesTheme.color.text.primary
    val closeIconColor = MercadoPagoAndesTheme.color.interactive.icon.idle

    return MessageDefaults(
        colors = MessageColorDefaults(
            backgroundColor = backgroundColor,
            textColor = textColor,
            closeIconColor = closeIconColor,
        ),
        spacing = MessageSpacingDefaults(
            horizontalPadding = MercadoPagoAndesTheme.spacing.paddings.xtiny,
            verticalPadding = MercadoPagoAndesTheme.spacing.paddings.xtiny,
            iconTextSpacing = MercadoPagoAndesTheme.spacing.gap.micro,
            closeIconSize = MercadoPagoAndesTheme.spacing.paddings.xsmall,
            closeIconPadding = MercadoPagoAndesTheme.spacing.paddings.xnano,
        ),
    )
}

/**
 * Message component - Displays toast-like messages with different types
 * This component uses Andes design tokens for colors, typography, and spacing
 *
 * @param text: Message text to display
 * @param modifier: Component modifier
 * @param type: Message type (Informative, Positive, Caution, or Negative)
 * @param onDismiss: Callback function executed when close button is clicked
 */
@Composable
fun MPMessage(
    text: String,
    modifier: Modifier = Modifier,
    type: MPMessageType = MPMessageType.Informative,
    onDismiss: () -> Unit = {},
) {
    val defaults = getMessageDefaults(type = type)
    val badgeType = when (type) {
        MPMessageType.Informative -> BadgeType.Informative
        MPMessageType.Positive -> BadgeType.Positive
        MPMessageType.Caution -> BadgeType.Caution
        MPMessageType.Negative -> BadgeType.Negative
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MercadoPagoAndesTheme.shape.xlarge)
            .background(defaults.colors.backgroundColor)
            .padding(
                horizontal = defaults.spacing.horizontalPadding,
                vertical = defaults.spacing.verticalPadding,
            ),
        verticalAlignment = Alignment.Top,
    ) {
        MPBadgeIcon(badgeType = badgeType)
        Spacer(modifier = Modifier.size(defaults.spacing.iconTextSpacing))
        MPText(
            text = text,
            style = MercadoPagoAndesTheme.typography.body.default.medium,
            color = defaults.colors.textColor,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.size(defaults.spacing.iconTextSpacing))
        Box(
            modifier = Modifier
                .size(defaults.spacing.closeIconSize)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.TopEnd,
        ) {
            Icon(
                painterResource(R.drawable.mp_icon_close_x),
                "",
                tint = defaults.colors.closeIconColor,
                modifier = Modifier.size(MercadoPagoAndesTheme.spacing.paddings.xtiny).padding(start = defaults.spacing.closeIconPadding),
            )
        }
    }
}

@Preview(name = "Message Informative", group = MESSAGE_GROUP)
@Composable
internal fun MessageInformativePreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "This can be a single or multiline text",
                type = MPMessageType.Informative,
            )
        }
    }
}

@Preview(name = "Message Positive", group = MESSAGE_GROUP)
@Composable
internal fun MessagePositivePreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "This can be a single or multiline text",
                type = MPMessageType.Positive,
            )
        }
    }
}

@Preview(name = "Message Caution", group = MESSAGE_GROUP)
@Composable
internal fun MessageCautionPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "This can be a single or multiline text",
                type = MPMessageType.Caution,
            )
        }
    }
}

@Preview(name = "Message Negative", group = MESSAGE_GROUP)
@Composable
internal fun MessageNegativePreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPMessage(
                text = "This can be a single or multiline text",
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
                text = "This can be a single or multiline text",
                type = MPMessageType.Informative,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPMessage(
                text = "This can be a single or multiline text",
                type = MPMessageType.Positive,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPMessage(
                text = "This can be a single or multiline text multiline text multiline text multiline text",
                type = MPMessageType.Caution,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MPMessage(
                text = "This can be a single or multiline text",
                type = MPMessageType.Negative,
            )
        }
    }
}
