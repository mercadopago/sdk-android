package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenBodyDataResponse
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenButtonStyle
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenIcon
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenListItemStyle
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSegmentState
import java.net.URI

private const val HTTPS_PREFIX = "https://"
private const val BASE64_PNG_DATA_URI_PREFIX = "data:image/png;base64,"
private const val ICON_COPY = "COPY"
private const val STYLE_LOUD = "loud"
private const val STYLE_QUIET = "quiet"
private const val STYLE_TRANSPARENT = "transparent"
private const val LEADING_TYPE_ICON = "icon"
private const val LEADING_ICON_BILL = "bill"
private const val SEGMENT_STATE_SUCCESS = "success"
private const val LIST_ITEM_STYLE_SIMPLE = "simple"

private val UNSAFE_HOST_PATTERNS = listOf(
    Regex("^localhost$", RegexOption.IGNORE_CASE),
    Regex("^127(\\.\\d{1,3}){3}$"),
    Regex("^10(\\.\\d{1,3}){3}$"),
    Regex("^172\\.(1[6-9]|2\\d|3[0-1])(\\.\\d{1,3}){2}$"),
    Regex("^192\\.168(\\.\\d{1,3}){2}$"),
    Regex("^169\\.254(\\.\\d{1,3}){2}$"),
    Regex("^0\\.0\\.0\\.0$"),
    Regex("^::1$"),
    Regex("^fe[89ab][0-9a-f]:.*$", RegexOption.IGNORE_CASE),
    Regex("^f[cd][0-9a-f]{2}:.*$", RegexOption.IGNORE_CASE),
)

private fun isUnsafeHost(
    host: String,
): Boolean {
    val normalizedHost = host.removePrefix("[").removeSuffix("]")
    return UNSAFE_HOST_PATTERNS.any { it.matches(normalizedHost) }
}

internal fun String?.asHttpsUrlOrNull(): String? =
    takeIf { !it.isNullOrBlank() && it.startsWith(HTTPS_PREFIX) }
        ?.takeIf { url -> runCatching { URI(url).host }.getOrNull()?.let { !isUnsafeHost(it) } == true }

internal fun String?.asBarcodeImageDataUriOrNull(): String? =
    takeIf {
        !it.isNullOrBlank() && it.startsWith(BASE64_PNG_DATA_URI_PREFIX)
    }

internal fun String?.toStatusScreenIconOrNull(): StatusScreenIcon? =
    when (this) {
        ICON_COPY -> StatusScreenIcon.Copy
        else -> null
    }

internal fun String?.toStatusScreenButtonStyleOrNull(): StatusScreenButtonStyle? =
    when (this) {
        STYLE_LOUD -> StatusScreenButtonStyle.Loud
        STYLE_QUIET -> StatusScreenButtonStyle.Quiet
        STYLE_TRANSPARENT -> StatusScreenButtonStyle.Transparent
        else -> null
    }

internal fun StatusScreenBodyDataResponse.toLeadingIconOrNull(): StatusScreenIcon? =
    when (leadingValue.takeIf { leadingType == LEADING_TYPE_ICON }) {
        LEADING_ICON_BILL -> StatusScreenIcon.Bill
        else -> null
    }

internal fun String?.toSegmentStateOrNull(): StatusScreenSegmentState? =
    when (this) {
        SEGMENT_STATE_SUCCESS -> StatusScreenSegmentState.Success
        else -> null
    }

internal fun String?.toListItemStyleOrNull(): StatusScreenListItemStyle? =
    when (this) {
        LIST_ITEM_STYLE_SIMPLE -> StatusScreenListItemStyle.Simple
        else -> null
    }
