package com.mercadopago.sdk.android.components.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents the leading content type for components that support an optional leading area.
 *
 * Use this sealed class to describe what should be rendered at the start (left side) of a row/item.
 */
sealed class MPListItemLeading {
    /**
     * Leading icon.
     * @param icon Icon vector drawable
     * @param tint Tint color applied to the icon. Use [Color.Unspecified] (default) to preserve
     * the original drawable colors, or pass an explicit color for monochrome icons.
     */
    data class Icon(val icon: ImageVector, val tint: Color = Color.Unspecified) : MPListItemLeading()

    /**
     * Leading thumbnail loaded from a remote URL.
     * @param url Image URL
     */
    data class Thumbnail(val url: String) : MPListItemLeading()
}
