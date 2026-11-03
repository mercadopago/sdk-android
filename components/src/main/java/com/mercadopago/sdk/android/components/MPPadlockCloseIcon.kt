package com.mercadopago.sdk.android.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/**
 * Padlock (closed) icon used on secure actions such as the review & confirm footer button.
 * Same artwork as the iOS `Padlock-Close` asset. Tinted by the component that renders it.
 */
val MPPadlockCloseIcon: ImageVector = ImageVector.Builder(
    name = "MPPadlockClose",
    defaultWidth = 20.dp,
    defaultHeight = 20.dp,
    viewportWidth = 20f,
    viewportHeight = 20f,
).apply {
    addPath(
        pathData = addPathNodes(
            """
            M9.37496 11.821
            C9.37496 11.476 9.65496 11.196 9.99996 11.196
            C10.345 11.196 10.625 11.476 10.625 11.821
            V14.321
            C10.625 14.666 10.345 14.946 9.99996 14.946
            C9.65496 14.946 9.37496 14.666 9.37496 14.321
            V11.821
            Z
            """.trimIndent(),
        ),
        fill = SolidColor(Color.Black),
    )
    addPath(
        pathData = addPathNodes(
            """
            M13.9583 5.98763
            V7.92596
            C15.3825 8.21596 16.4583 9.47846 16.4583 10.9876
            V15.1543
            C16.4583 16.8768 15.0558 18.2793 13.3333 18.2793
            H6.66663
            C4.94329 18.2793 3.54163 16.8768 3.54163 15.1543
            V10.9876
            C3.54163 9.47846 4.61663 8.21596 6.04163 7.92596
            V5.98763
            C6.04163 3.80513 7.81746 2.0293 9.99996 2.0293
            C12.1825 2.0293 13.9583 3.80513 13.9583 5.98763
            Z
            M12.7083 5.98763
            C12.7083 4.4943 11.4933 3.2793 9.99996 3.2793
            C8.50663 3.2793 7.29163 4.4943 7.29163 5.98763
            V7.86263
            H12.7083
            V5.98763
            Z
            M13.3333 17.0293
            C14.3666 17.0293 15.2083 16.1876 15.2083 15.1543
            V10.9876
            C15.2083 9.95346 14.3666 9.11263 13.3333 9.11263
            H6.66663
            C5.63246 9.11263 4.79163 9.95346 4.79163 10.9876
            V15.1543
            C4.79163 16.1876 5.63246 17.0293 6.66663 17.0293
            H13.3333
            Z
            """.trimIndent(),
        ),
        fill = SolidColor(Color.Black),
        pathFillType = PathFillType.EvenOdd,
    )
}.build()
