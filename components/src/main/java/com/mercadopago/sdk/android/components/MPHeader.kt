package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

/**
 * A header component that provides different layout styles for page headers.
 *
 * The component supports three different header types through [headerType]:
 * - [MPHeaderType.ScrollOff]: Displays back button, large title, and optional subtitle in a column layout.
 * - [MPHeaderType.ScrollOn]: Displays back button and medium title in a row layout.
 * - [MPHeaderType.TittleLeft]: Displays only medium title without back button.
 *
 * @param modifier Modifier to be applied to the header container.
 * @param title The title text displayed in the header. Required parameter.
 * @param subtitle Optional subtitle text displayed below the title.
 * Only visible when [headerType] is [MPHeaderType.ScrollOff].
 * @param onBackClick Callback invoked when the back button is clicked.
 * Only active when [headerType] is [MPHeaderType.ScrollOff] or [MPHeaderType.ScrollOn].
 * @param headerType The type of header layout to display. Defaults to [MPHeaderType.ScrollOff].
 * @param content The composable content to display below the header.
 */
@Composable
fun MPHeader(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String = "",
    onBackClick: () -> Unit = {},
    headerType: MPHeaderType = MPHeaderType.ScrollOff,
    content: @Composable () -> Unit,
) {
    val defaults = getMPHeaderDefaults()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MercadoPagoAndesTheme.color.background.primary),
    ) {
        when (headerType) {
            MPHeaderType.ScrollOff -> {
                Column(
                    modifier = Modifier.padding(MercadoPagoAndesTheme.spacing.paddings.xtiny),
                ) {
                    HeaderBackButton {
                        onBackClick.invoke()
                    }
                    Spacer(modifier = Modifier.size(MercadoPagoAndesTheme.spacing.paddings.xmicro))
                    MPText(
                        text = title,
                        style = MercadoPagoAndesTheme.typography.heading.default.huge,
                    )
                    Spacer(modifier = Modifier.size(MercadoPagoAndesTheme.spacing.paddings.xmicro))
                    MPText(
                        text = subtitle,
                        style = MercadoPagoAndesTheme.typography.body.default.medium,
                    )
                }
            }

            MPHeaderType.ScrollOn -> {
                Row(
                    modifier = Modifier.padding(MercadoPagoAndesTheme.spacing.paddings.xtiny),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HeaderBackButton {
                        onBackClick.invoke()
                    }
                    Spacer(modifier = Modifier.size(MercadoPagoAndesTheme.spacing.paddings.xmicro))
                    MPText(
                        text = title,
                        style = MercadoPagoAndesTheme.typography.heading.default.medium,
                    )
                }
            }

            MPHeaderType.TittleLeft -> {
                Column(
                    modifier = Modifier.padding(MercadoPagoAndesTheme.spacing.paddings.xtiny),
                ) {
                    MPText(
                        text = title,
                        style = MercadoPagoAndesTheme.typography.heading.default.medium,
                    )
                }
            }
        }

        content.invoke()
    }
}

@Composable
private fun HeaderBackButton(
    modifier: Modifier = Modifier,
    defaults: MPHeaderDefaults = getMPHeaderDefaults(),
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(MercadoPagoAndesTheme.spacing.gap.medium)
            .clip(MercadoPagoAndesTheme.shape.tiny)
            .background(defaults.colors.backButtonBackground)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_left),
            contentDescription = "Back",
            tint = defaults.colors.backButtonIcon,
            modifier = Modifier.size(24.dp),
        )
    }
}

/**
 * Enum representing the different types of header layouts.
 */
enum class MPHeaderType {
    /** Header with back button, large title, and subtitle displayed in a column layout. */
    ScrollOff,

    /** Header with back button and medium title displayed in a row layout.*/
    ScrollOn,

    /** Header with only medium title, no back button.*/
    TittleLeft,
}

@Preview(name = "MPHeader Preview", group = "HEADER")
@Composable
private fun MPHeaderPreview() {
    MercadoPagoTheme {
        MPHeader(
            title = "Page Title",
            subtitle = "Label",
            onBackClick = {},
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(Int.MAX_VALUE) { index ->
                    MPText(
                        text = "Item $index",
                        style = MercadoPagoAndesTheme.typography.body.default.medium,
                        color = MercadoPagoAndesTheme.color.text.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}
