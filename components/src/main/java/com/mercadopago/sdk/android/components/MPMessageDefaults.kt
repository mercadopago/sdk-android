package com.mercadopago.sdk.android.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme

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
internal fun getMessageDefaults(
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
