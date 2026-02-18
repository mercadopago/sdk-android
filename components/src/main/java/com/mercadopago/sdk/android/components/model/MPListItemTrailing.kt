package com.mercadopago.sdk.android.components.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Trailing model class, used to determine the trailing showing type
 * This its used to change the component showed in trailing
 *
 * @param text: Trailing text
 * @param type: Trailing type
 * @param textColor: Trailing text color
 */
data class MPListItemTrailing(
    val text: String? = null,
    val type: Type? = null,
    val textColor: Color? = null,
) {
    /**
     * Represents the trailing content type for components that support an optional trailing area.
     *
     * Use this sealed class to describe what should be rendered at the end (right side) of a row/item.
     */
    sealed class Type {
        /**
         *  Trailing icon.
         *  @param icon Trailing icon
         */
        data class Icon(val icon: ImageVector) : Type()

        /**
         * No trailing content.
         */
        data object None : Type()
    }
}
