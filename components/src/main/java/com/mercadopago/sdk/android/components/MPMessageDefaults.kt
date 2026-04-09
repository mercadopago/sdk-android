package com.mercadopago.sdk.android.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

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
        MPMessageType.Informative -> MercadoPagoTheme.color.feedback.informative
        MPMessageType.Positive -> MercadoPagoTheme.color.feedback.positive
        MPMessageType.Caution -> MercadoPagoTheme.color.feedback.caution
        MPMessageType.Negative -> MercadoPagoTheme.color.feedback.negative
    }
    val backgroundColor = feedbackColors.fillQuiet
    val textColor = MercadoPagoTheme.color.text.primary
    val closeIconColor = MercadoPagoTheme.color.interactive.icon.idle
    return MessageDefaults(
        colors = MessageColorDefaults(
            backgroundColor = backgroundColor,
            textColor = textColor,
            closeIconColor = closeIconColor,
        ),
        spacing = MessageSpacingDefaults(
            horizontalPadding = MercadoPagoTheme.spacing.paddings.xtiny,
            verticalPadding = MercadoPagoTheme.spacing.paddings.xtiny,
            iconTextSpacing = MercadoPagoTheme.spacing.gap.micro,
            closeIconSize = MercadoPagoTheme.spacing.paddings.xsmall,
            closeIconPadding = MercadoPagoTheme.spacing.paddings.xnano,
        ),
    )
}
