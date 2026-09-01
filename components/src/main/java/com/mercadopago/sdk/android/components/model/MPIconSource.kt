package com.mercadopago.sdk.android.components.model

import androidx.annotation.DrawableRes

/**
 * Sealed interface representing the different sources from which an icon can be loaded.
 */
sealed interface MPIconSource {
    /**
     * Icon loaded from a drawable resource.
     *
     * @param resId The drawable resource ID.
     */
    data class Resource(
        @DrawableRes val resId: Int,
    ) : MPIconSource

    /**
     * Icon loaded from a remote URL.
     *
     * @param url The URL to load the icon from. If null, will display a placeholder or error state.
     * @param applyTint Whether to apply color tint. Set to false for colorful logos. Defaults to true.
     */
    data class Remote(
        val url: String?,
        val applyTint: Boolean = true,
    ) : MPIconSource
}
