package com.mercadopago.sdk.android.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mercadopago.sdk.android.components.model.MPIconColor
import com.mercadopago.sdk.android.components.model.MPIconSize
import com.mercadopago.sdk.android.components.model.MPIconSource
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

/**
 * A composable that displays an icon from various sources (drawable resource or remote URL).
 * Supports different sizes, colors, and handles loading/error states for remote icons.
 *
 * This component follows the same API as the iOS MPIcon counterpart, providing a consistent
 * cross-platform experience.
 *
 * @param source The source of the icon (resource or remote URL).
 * @param modifier The modifier to be applied to the icon.
 * @param size The size of the icon. Defaults to [MPIconSize.Medium].
 * @param color The color of the icon. Defaults to [MPIconColor.Primary].
 * @param contentDescription The content description for accessibility. If null and [isDecorative] is false,
 * a default description will be used.
 * @param isDecorative Whether this icon is purely decorative and should be hidden from accessibility services.
 * Defaults to false.
 */
@Composable
fun MPIcon(
    source: MPIconSource,
    modifier: Modifier = Modifier,
    size: MPIconSize = MPIconSize.Medium,
    color: MPIconColor = MPIconColor.Primary,
    contentDescription: String? = null,
    isDecorative: Boolean = false,
) {
    val iconModifier = modifier
        .size(size.size)
        .then(
            if (isDecorative) {
                Modifier.semantics { this.contentDescription = "" }
            } else {
                Modifier
            },
        )

    when (source) {
        is MPIconSource.Resource -> {
            Icon(
                painter = painterResource(source.resId),
                contentDescription = if (isDecorative) null else contentDescription,
                modifier = iconModifier,
                tint = color.toColor(),
            )
        }

        is MPIconSource.Remote -> {
            RemoteIcon(
                url = source.url,
                modifier = iconModifier,
                size = size,
                color = color,
                applyTint = source.applyTint,
                contentDescription = if (isDecorative) null else contentDescription,
            )
        }
    }
}

/**
 * Convenience constructor for creating an icon from a drawable resource.
 *
 * @param resId The drawable resource ID.
 * @param modifier The modifier to be applied to the icon.
 * @param size The size of the icon. Defaults to [MPIconSize.Medium].
 * @param color The color of the icon. Defaults to [MPIconColor.Primary].
 * @param contentDescription The content description for accessibility.
 * @param isDecorative Whether this icon is purely decorative. Defaults to false.
 */
@Composable
fun MPIcon(
    @DrawableRes resId: Int,
    modifier: Modifier = Modifier,
    size: MPIconSize = MPIconSize.Medium,
    color: MPIconColor = MPIconColor.Primary,
    contentDescription: String? = null,
    isDecorative: Boolean = false,
) {
    MPIcon(
        source = MPIconSource.Resource(resId),
        modifier = modifier,
        size = size,
        color = color,
        contentDescription = contentDescription,
        isDecorative = isDecorative,
    )
}

/**
 * Convenience constructor for creating an icon from a remote URL.
 *
 * @param url The URL to load the icon from. If null, will display an error state.
 * @param modifier The modifier to be applied to the icon.
 * @param size The size of the icon. Defaults to [MPIconSize.Medium].
 * @param color The color of the icon. Defaults to [MPIconColor.Primary].
 * @param applyTint Whether to apply color tint. Set to false for colorful logos. Defaults to true.
 * @param showBorder Whether to show a circular border around the icon. Defaults to false.
 * @param contentDescription The content description for accessibility.
 * @param isDecorative Whether this icon is purely decorative. Defaults to false.
 */
@Composable
fun MPIcon(
    url: String?,
    modifier: Modifier = Modifier,
    size: MPIconSize = MPIconSize.Medium,
    color: MPIconColor = MPIconColor.Primary,
    applyTint: Boolean = true,
    showBorder: Boolean = false,
    contentDescription: String? = null,
    isDecorative: Boolean = false,
) {
    if (!showBorder) {
        MPIcon(
            source = MPIconSource.Remote(url, applyTint),
            modifier = modifier,
            size = size,
            color = color,
            contentDescription = contentDescription,
            isDecorative = isDecorative,
        )
    } else {
        Box(
            modifier = modifier
                .size(size.size)
                .border(
                    width = 1.dp,
                    color = MercadoPagoTheme.color.border.primary,
                    shape = CircleShape,
                )
                .background(
                    color = MercadoPagoTheme.color.background.primary,
                    shape = CircleShape,
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (url != null) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    contentDescription = if (isDecorative) null else contentDescription,
                    modifier = Modifier
                        .size(size.size - 8.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit,
                    colorFilter = if (applyTint) ColorFilter.tint(color.toColor()) else null,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier.size(size.size / 2),
                            color = color.toColor(),
                            strokeWidth = 2.dp,
                        )
                    },
                    error = {
                        Icon(
                            painter = painterResource(R.drawable.ic_feedback_error),
                            contentDescription = contentDescription,
                            tint = if (applyTint) color.toColor() else Color.Unspecified,
                        )
                    },
                )
            }
        }
    }
}

@Suppress("LongParameterList")
@Composable
private fun RemoteIcon(
    url: String?,
    modifier: Modifier,
    size: MPIconSize,
    color: MPIconColor,
    applyTint: Boolean,
    contentDescription: String?,
) {
    if (url == null) {
        Icon(
            painter = painterResource(R.drawable.ic_feedback_error),
            contentDescription = contentDescription,
            modifier = modifier,
            tint = if (applyTint) color.toColor() else Color.Unspecified,
        )
        return
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Fit,
        colorFilter = if (applyTint) ColorFilter.tint(color.toColor()) else null,
        loading = {
            CircularProgressIndicator(
                modifier = Modifier.size(size.size / 2),
                color = color.toColor(),
                strokeWidth = 2.dp,
            )
        },
        error = {
            Icon(
                painter = painterResource(R.drawable.ic_feedback_error),
                contentDescription = contentDescription,
                tint = if (applyTint) color.toColor() else Color.Unspecified,
            )
        },
    )
}

@Suppress("LongMethod")
@Preview(showBackground = true)
@Composable
private fun MPIconPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MPText("Resource Icons - Different Sizes")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MPIcon(
                    resId = R.drawable.ic_feedback_positive,
                    size = MPIconSize.XSmall,
                    color = MPIconColor.Positive,
                )
                MPIcon(
                    resId = R.drawable.ic_feedback_positive,
                    size = MPIconSize.Small,
                    color = MPIconColor.Positive,
                )
                MPIcon(
                    resId = R.drawable.ic_feedback_positive,
                    size = MPIconSize.Medium,
                    color = MPIconColor.Positive,
                )
                MPIcon(
                    resId = R.drawable.ic_feedback_positive,
                    size = MPIconSize.Large,
                    color = MPIconColor.Positive,
                )
                MPIcon(
                    resId = R.drawable.ic_feedback_positive,
                    size = MPIconSize.XLarge,
                    color = MPIconColor.Positive,
                )
            }

            MPText("Resource Icons - Different Colors")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MPIcon(
                    resId = R.drawable.ic_feedback_positive,
                    color = MPIconColor.Primary,
                )
                MPIcon(
                    resId = R.drawable.ic_feedback_caution,
                    color = MPIconColor.Caution,
                )
                MPIcon(
                    resId = R.drawable.ic_feedback_error,
                    color = MPIconColor.Negative,
                )
                MPIcon(
                    resId = R.drawable.ic_feedback_info,
                    color = MPIconColor.Informative,
                )
            }

            MPText("Remote Icon (URL)")
            MPIcon(
                url = "https://http2.mlstatic.com/storage/mobile-on-demand-resources/" +
                    "image/brick-payment-method-visa_3X",
                size = MPIconSize.Large,
                color = MPIconColor.Primary,
            )

            MPText("Remote Icon (null URL - error state)")
            MPIcon(
                url = null,
                size = MPIconSize.Medium,
                color = MPIconColor.Negative,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MPIconSourcePreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MPText("Using MPIconSource")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MPIcon(
                    source = MPIconSource.Resource(R.drawable.ic_feedback_positive),
                    size = MPIconSize.Medium,
                    color = MPIconColor.Positive,
                )

                MPIcon(
                    source = MPIconSource.Remote(
                        "https://http2.mlstatic.com/storage/mobile-on-demand-resources/" +
                            "image/brick-payment-method-visa_3X",
                    ),
                    size = MPIconSize.Medium,
                    color = MPIconColor.Primary,
                )
            }
        }
    }
}
