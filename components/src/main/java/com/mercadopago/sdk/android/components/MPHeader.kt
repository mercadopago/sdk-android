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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

/**
 * A header component that provides different layout styles for page headers.
 *
 * The component supports three different header types through [headerType]:
 * - [MPHeaderType.ScrollOff]: The expanded header (back button, large title, optional subtitle) is placed
 *   above the content and scrolls up with it. A collapsed bar (back + medium title) is shown as an overlay
 *   at the top, fading from transparent to fully visible as the user scrolls. When using ScrollOff, do not
 *   apply [androidx.compose.foundation.verticalScroll] to the content passed in [content]; the scroll is
 *   owned by this component.
 * - [MPHeaderType.ScrollOn]: Displays back button and medium title in a fixed row layout.
 * - [MPHeaderType.TittleLeft]: Displays only medium title without back button.
 *
 * @param modifier Modifier to be applied to the header container.
 * @param title The title text displayed in the header. Required parameter.
 * @param subtitle Optional subtitle text displayed below the title.
 * Only visible when [headerType] is [MPHeaderType.ScrollOff].
 * @param onBackClick Callback invoked when the back button is clicked.
 * Only active when [headerType] is [MPHeaderType.ScrollOff] or [MPHeaderType.ScrollOn].
 * @param headerType The type of header layout to display. Defaults to [MPHeaderType.ScrollOn].
 * @param content The composable content to display below the header. When [headerType] is [MPHeaderType.ScrollOff],
 * this content is placed inside the same scroll as the expanded header; do not add verticalScroll to it.
 */
@Composable
fun MPHeader(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String = "",
    onBackClick: () -> Unit = {},
    headerType: MPHeaderType = MPHeaderType.ScrollOn,
    content: @Composable () -> Unit,
) {
    when (headerType) {
        MPHeaderType.ScrollOff -> {
            MPHeaderScrollOffContent(
                modifier = modifier,
                title = title,
                subtitle = subtitle,
                onBackClick = onBackClick,
                content = content,
            )
        }
        MPHeaderType.ScrollOn -> {
            MPHeaderSingleLayoutContent(
                modifier = modifier,
                headerType = headerType,
                title = title,
                subtitle = subtitle,
                onBackClick = onBackClick,
                content = content,
            )
        }
        MPHeaderType.TittleLeft -> {
            MPHeaderSingleLayoutContent(
                modifier = modifier,
                headerType = headerType,
                title = title,
                subtitle = subtitle,
                onBackClick = onBackClick,
                content = content,
            )
        }
    }
}

@Composable
private fun MPHeaderScrollOffContent(
    modifier: Modifier,
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val scrollState = rememberScrollState()
    var titleBlockHeightPx by remember { mutableStateOf(0f) }
    val scrollOffset = scrollState.value.toFloat()
    val progress = if (titleBlockHeightPx > 0) {
        (scrollOffset / titleBlockHeightPx).coerceIn(0f, 1f)
    } else {
        0f
    }
    Box(
        modifier = modifier
            .background(color = MercadoPagoAndesTheme.color.background.primary),
    ) {
        Column(
            modifier = Modifier.verticalScroll(scrollState),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MercadoPagoAndesTheme.spacing.paddings.xtiny)
                    .padding(
                        top = MercadoPagoAndesTheme.spacing.gap.medium +
                            MercadoPagoAndesTheme.spacing.paddings.xtiny * 2,
                    )
                    .onGloballyPositioned { coordinates ->
                        titleBlockHeightPx = coordinates.size.height.toFloat()
                    },
            ) {
                MPText(
                    text = title,
                    style = MercadoPagoAndesTheme.typography.heading.default.huge,
                )
                if (subtitle.isNotBlank()) {
                    Spacer(modifier = Modifier.size(MercadoPagoAndesTheme.spacing.paddings.xmicro))
                    MPText(
                        text = subtitle,
                        style = MercadoPagoAndesTheme.typography.body.default.medium,
                    )
                }
            }
            content.invoke()
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .background(color = MercadoPagoAndesTheme.color.background.primary)
                .padding(MercadoPagoAndesTheme.spacing.paddings.xtiny),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HeaderBackButton {
                onBackClick.invoke()
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(progress),
                contentAlignment = Alignment.Center,
            ) {
                MPText(
                    text = title,
                    style = MercadoPagoAndesTheme.typography.heading.default.medium,
                )
            }
        }
    }
}

@Composable
private fun MPHeaderSingleLayoutContent(
    modifier: Modifier,
    headerType: MPHeaderType,
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MercadoPagoAndesTheme.color.background.primary),
    ) {
        when (headerType) {
            MPHeaderType.ScrollOn -> {
                Row(
                    modifier = Modifier
                        .padding(MercadoPagoAndesTheme.spacing.paddings.xtiny)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HeaderBackButton {
                        onBackClick.invoke()
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        MPText(
                            text = title,
                            style = MercadoPagoAndesTheme.typography.heading.default.medium,
                        )
                    }
                }
            }
            MPHeaderType.TittleLeft -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MercadoPagoAndesTheme.spacing.paddings.xtiny),
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        MPText(
                            text = title,
                            style = MercadoPagoAndesTheme.typography.heading.default.medium,
                        )
                    }
                }
            }
            MPHeaderType.ScrollOff -> { }
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

@Preview(name = "MPHeader ScrollOff", group = "HEADER")
@Composable
private fun MPHeaderScrollOffPreview() {
    MercadoPagoTheme {
        MPHeader(
            title = "Page Title",
            subtitle = "Label",
            onBackClick = {},
            headerType = MPHeaderType.ScrollOff,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                repeat(20) { index ->
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

@Preview(name = "MPHeader ScrollOn", group = "HEADER")
@Composable
private fun MPHeaderScrollOnPreview() {
    MercadoPagoTheme {
        MPHeader(
            title = "Page Title",
            onBackClick = {},
            headerType = MPHeaderType.ScrollOn,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(20) { index ->
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
