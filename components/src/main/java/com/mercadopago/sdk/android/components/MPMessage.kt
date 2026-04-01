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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import kotlinx.coroutines.delay

private const val MESSAGE_GROUP = "Message"
private const val DURATION_SHORT_MILLIS = 3000L
private const val DURATION_MEDIUM_MILLIS = 6000L
private const val DURATION_LONG_MILLIS = 10000L

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
 * Message Duration enum class, used to determine how long the message should be displayed
 * This controls the auto-dismiss behavior of the toast-like message
 * @param durationMillis Component dismiss call durations
 */
enum class MPMessageDuration(val durationMillis: kotlin.Long?) {
    /**
     * Short: Message is displayed for 3 seconds
     */
    Short(DURATION_SHORT_MILLIS),

    /**
     * Normal: Message is displayed for 6 seconds
     */
    Normal(DURATION_MEDIUM_MILLIS),

    /**
     * Long: Message is displayed for 10 seconds
     */
    Long(DURATION_LONG_MILLIS),

    /**
     * Indefinite: Message does not auto-dismiss and remains visible until manually closed
     */
    Indefinite(null),
}

/**
 * Message component - Displays toast-like messages with different types
 * This component uses Andes design tokens for colors, typography, and spacing
 *
 * @param text: Message text to display
 * @param modifier: Component modifier
 * @param type: Message type (Informative, Positive, Caution, or Negative)
 * @param duration: Duration for auto-dismiss behavior (Short: 3s, Medium: 6s, Long: 10s, Indefinite: no auto-dismiss)
 * @param onDismiss: Callback function executed when close button is clicked or when auto-dismiss timer expires
 */
@Composable
fun MPMessage(
    text: String,
    modifier: Modifier = Modifier,
    type: MPMessageType = MPMessageType.Informative,
    duration: MPMessageDuration = MPMessageDuration.Short,
    onDismiss: () -> Unit = {},
) {
    val defaults = getMessageDefaults(type = type)
    val badgeType = when (type) {
        MPMessageType.Informative -> BadgeType.Informative
        MPMessageType.Positive -> BadgeType.Positive
        MPMessageType.Caution -> BadgeType.Caution
        MPMessageType.Negative -> BadgeType.Negative
    }

    LaunchedEffect(duration) {
        duration.durationMillis?.let { durationMillis ->
            delay(durationMillis)
            onDismiss()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MercadoPagoTheme.shape.xlarge)
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
            style = MercadoPagoTheme.typography.body.default.medium,
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
                modifier = Modifier.size(
                    MercadoPagoTheme.spacing.paddings.xtiny,
                ).padding(start = defaults.spacing.closeIconPadding),
            )
        }
    }
}

@Preview(name = "Message All Variations", group = MESSAGE_GROUP)
@Composable
internal fun MessageAllVariationsPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
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
