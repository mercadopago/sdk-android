package com.mercadopago.sdk.android.components.model

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Enum representing the available icon shape options.
 */
enum class MPIconShape {
    /** Circular shape. */
    Circle,

    /** Rounded rectangle shape (8dp corner radius). */
    RoundedCorner,

    /** Rectangle shape (no rounded corners). */
    Rectangle,
    ;

    /**
     * Converts this shape enum to a Compose Shape.
     */
    fun toShape(): Shape =
        when (this) {
            Circle -> CircleShape
            RoundedCorner -> RoundedCornerShape(8.dp)
            Rectangle -> RectangleShape
        }
}
