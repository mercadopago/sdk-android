package com.mercadopago.sdk.android.components.model

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
     */
    data class Icon(val icon: ImageVector) : MPListItemLeading()

    /**
     * Leading thumbnail loaded from a remote URL.
     * @param url Image URL
     */
    data class Thumbnail(val url: String) : MPListItemLeading()
}
