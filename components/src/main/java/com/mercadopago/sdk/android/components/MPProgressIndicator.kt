package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.extensions.circularSize
import com.mercadopago.sdk.android.components.extensions.strokeWidth
import com.mercadopago.sdk.android.components.model.MPProgressIndicatorSize
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val PROGRESS_INDICATOR_GROUP = "PROGRESS_INDICATOR"

/**
 * Progress Indicator component - Displays circular progress loading state
 * This component is used to show loading states throughout the application
 * supporting both determinate and indeterminate progress
 *
 * @param modifier: progress indicator modifier
 * @param size: indicator size (XSmall, Small, Medium, Large, XLarge)
 */
@Composable
fun MPProgressIndicator(
    modifier: Modifier = Modifier,
    size: MPProgressIndicatorSize = MPProgressIndicatorSize.Medium,
) {
    CircularProgressIndicator(
        modifier = modifier.size(size.circularSize),
        color = MercadoPagoAndesTheme.color.interactive.fillLoud.idle,
        strokeWidth = size.strokeWidth,
        trackColor = Color.Transparent,
        strokeCap = StrokeCap.Round,
    )
}

@Preview(name = "Circular Progress Indeterminate", group = PROGRESS_INDICATOR_GROUP)
@Composable
private fun MPProgressIndicatorPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes,
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPProgressIndicator(
                size = MPProgressIndicatorSize.XSmall,
            )
            Spacer(Modifier.size(16.dp))
            MPProgressIndicator(
                size = MPProgressIndicatorSize.Small,
            )
            Spacer(Modifier.size(16.dp))
            MPProgressIndicator(
                size = MPProgressIndicatorSize.Medium,
            )
            Spacer(Modifier.size(16.dp))
            MPProgressIndicator(
                size = MPProgressIndicatorSize.Large,
            )
            Spacer(Modifier.size(16.dp))
            MPProgressIndicator(
                size = MPProgressIndicatorSize.XLarge,
            )
        }
    }
}
