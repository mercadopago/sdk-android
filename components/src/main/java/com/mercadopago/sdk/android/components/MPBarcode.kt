package com.mercadopago.sdk.android.components

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.components.extensions.toValidatedBitmapOrNull
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val BARCODE_GROUP = "Barcode"
private const val COPY_FEEDBACK_DURATION_MILLIS = 2000L

/**
 * Barcode component - Renders a digitable-line barcode with a mandatory copy action
 * and an optional PNG image region, rendered only when the image data is present and
 * passes bounded decode validation.
 *
 * @param content raw digitable line copied to the clipboard when the copy action is tapped
 * @param codeFormatted display-formatted digitable line
 * @param copyLabel label shown on the copy action before it is tapped
 * @param copyFeedback label shown briefly after the copy action is tapped
 * @param modifier component modifier
 * @param imageDataUri optional base64 PNG data URI; rendered only when it decodes within bounds
 * @param label optional heading rendered above the formatted code
 * @param icon copy action icon; when null, the copy action is hidden
 * @param onCopy callback invoked right after the content is copied to the clipboard
 */
@Composable
fun MPBarcode(
    content: String,
    codeFormatted: String,
    copyLabel: String,
    copyFeedback: String,
    modifier: Modifier = Modifier,
    imageDataUri: String? = null,
    label: String? = null,
    @DrawableRes icon: Int? = null,
    onCopy: () -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current
    var copyRequestId by remember { mutableIntStateOf(0) }
    var justCopied by remember { mutableStateOf(false) }

    LaunchedEffect(copyRequestId) {
        if (copyRequestId > 0) {
            justCopied = true
            delay(COPY_FEEDBACK_DURATION_MILLIS)
            justCopied = false
        }
    }

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(imageDataUri) {
        bitmap = withContext(Dispatchers.Default) { imageDataUri.toValidatedBitmapOrNull() }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.gap.xmicro),
    ) {
        bitmap?.let { safeBitmap ->
            Image(
                bitmap = safeBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        label?.let {
            MPText(
                text = it,
                style = MercadoPagoTheme.typography.body.default.large,
                color = MercadoPagoTheme.color.text.primary,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.gap.xmicro),
        ) {
            MPText(
                text = codeFormatted,
                modifier = Modifier.weight(1f),
                style = MercadoPagoTheme.typography.body.emphasis.large,
                color = MercadoPagoTheme.color.text.primary,
            )
            icon?.let { safeIcon ->
                Icon(
                    painter = painterResource(safeIcon),
                    contentDescription = if (justCopied) copyFeedback else copyLabel,
                    tint = MercadoPagoTheme.color.icon.primary,
                    modifier = Modifier.size(MercadoPagoTheme.spacing.paddings.xtiny).clickable {
                        clipboardManager.setText(AnnotatedString(content))
                        copyRequestId++
                        onCopy()
                    },
                )
            }
        }
    }
}

@Preview(name = "Barcode - without copy icon", group = BARCODE_GROUP, showBackground = true)
@Composable
private fun MPBarcodeTextOnlyPreview() {
    MercadoPagoTheme {
        MPBarcode(
            content = "34191.75537 05496.638292 71000.063305 1 96430000090000",
            codeFormatted = "34191.75537 05496.638292 71000.063305 1 96430000090000",
            copyLabel = "Copiar código",
            copyFeedback = "Código copiado",
        )
    }
}

@Preview(name = "Barcode - with copy icon", group = BARCODE_GROUP, showBackground = true)
@Composable
private fun MPBarcodeWithCopyIconPreview() {
    MercadoPagoTheme {
        MPBarcode(
            content = "34191.75537 05496.638292 71000.063305 1 96430000090000",
            codeFormatted = "34191.75537 05496.638292 71000.063305 1 96430000090000",
            copyLabel = "Copiar código",
            copyFeedback = "Código copiado",
            icon = R.drawable.ic_copy,
        )
    }
}
