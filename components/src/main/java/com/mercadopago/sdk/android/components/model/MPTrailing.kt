package com.mercadopago.sdk.android.components.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Trailing model class, used to determine the trailing showing type
 * This its used to change the component showed in trailing
 *
 * @param text: Trailing text
 * @param icon: Trailing icon
 * @param textColor: Trailing text color
 */
data class MPTrailing(
    val text: String? = null,
    val icon: ImageVector? = null,
    val textColor: Color? = null,
)
