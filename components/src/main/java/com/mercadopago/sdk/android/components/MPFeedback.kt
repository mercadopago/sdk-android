package com.mercadopago.sdk.android.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val FEEDBACK_GROUP = "Feedback"

/**
 * Feedback component - Displays a remote status icon with a title, used to communicate the
 * outcome of a flow (e.g. an approved payment). The icon has no local fallback: when it fails
 * to load, only the icon area is affected and the title remains visible.
 *
 * @param title feedback title rendered below the icon
 * @param iconUrl remote icon URL
 * @param modifier component modifier
 * @param subtitle optional text rendered below the title
 */
@Composable
fun MPFeedback(
    title: String,
    iconUrl: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    MPFeedbackLayout(title = title, subtitle = subtitle, modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(iconUrl)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(MercadoPagoTheme.spacing.paddings.xhuge),
        )
    }
}

/**
 * Feedback component with a bundled icon, for when the icon must be available without network.
 *
 * @param title feedback title rendered below the icon
 * @param iconRes drawable resource of the icon
 * @param modifier component modifier
 * @param subtitle optional text rendered below the title
 */
@Composable
fun MPFeedback(
    title: String,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    MPFeedbackLayout(title = title, subtitle = subtitle, modifier = modifier) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(MercadoPagoTheme.spacing.paddings.xhuge),
        )
    }
}

@Composable
private fun MPFeedbackLayout(
    title: String,
    subtitle: String?,
    modifier: Modifier,
    icon: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        icon()
        if (title.isNotBlank()) {
            Spacer(modifier = Modifier.size(MercadoPagoTheme.spacing.gap.xtiny))
            MPText(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                style = MercadoPagoTheme.typography.heading.default.medium.copy(textAlign = TextAlign.Start),
                color = MercadoPagoTheme.color.text.primary,
            )
        }
        subtitle?.let {
            Spacer(modifier = Modifier.size(MercadoPagoTheme.spacing.gap.xmicro))
            MPText(
                text = it,
                modifier = Modifier.fillMaxWidth(),
                style = MercadoPagoTheme.typography.body.default.medium.copy(textAlign = TextAlign.Start),
                color = MercadoPagoTheme.color.text.secondary,
            )
        }
    }
}

@Preview(name = "Feedback", group = FEEDBACK_GROUP, showBackground = true)
@Composable
private fun MPFeedbackPreview() {
    MercadoPagoTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            MPFeedback(
                title = "Pagaste (\$ 900)",
                iconUrl = "https://http.cat/200",
            )
            MPFeedback(
                title = "Tu pago fue rechazado",
                iconUrl = "https://http.cat/402",
                subtitle = "Santander Crédito •••• 1234",
            )
        }
    }
}
