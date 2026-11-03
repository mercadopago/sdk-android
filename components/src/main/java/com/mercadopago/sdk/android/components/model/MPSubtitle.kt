package com.mercadopago.sdk.android.components.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Data class representing the subtitle configuration for components.
 *
 * @property text The subtitle text to display
 * @property color Optional color override for the subtitle text
 * @property content Optional composable content for custom subtitle rendering
 */
data class MPSubtitle(
    val text: String? = null,
    val color: Color? = null,
    val content: (@Composable () -> Unit)? = null,
)
