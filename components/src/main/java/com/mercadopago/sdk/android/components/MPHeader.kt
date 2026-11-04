package com.mercadopago.sdk.android.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.extensions.scrollProgressRatio
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

/**
 * A header component that provides different layout styles for page headers.
 *
 * Displays an expanded header (back button, large title, optional subtitle) above the content that
 * scrolls up with it. A collapsed bar (back + medium title) is shown as an overlay at the top,
 * fading from transparent to fully visible as the user scrolls.
 *
 * @param modifier Modifier to be applied to the header container.
 * @param scrollState The [ScrollState] driving the internal scroll. Hoist this state when callers
 * need to observe or react to the scroll offset (e.g. to position overlays relative to scrollable
 * content). Defaults to an internally-owned [rememberScrollState].
 * @param title The title text displayed in the header.
 * @param subtitle Optional subtitle text displayed below the title.
 * @param onBackClick Callback invoked when the back button is clicked.
 * @param content Composable content placed below the expanded title, inside the same scroll
 * container. Do not apply [androidx.compose.foundation.verticalScroll] to this content.
 */
@Composable
fun MPHeader(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    title: String,
    subtitle: String = "",
    onBackClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    var titleBlockHeightPx by remember { mutableFloatStateOf(0f) }
    var collapsedBarHeightPx by remember { mutableFloatStateOf(0f) }
    val scrollOffset = scrollState.value.toFloat()
    val progress = scrollOffset.scrollProgressRatio(titleBlockHeightPx)
    val inlineTitleOpacity = ((progress - INLINE_TITLE_THRESHOLD) / (1f - INLINE_TITLE_THRESHOLD)).coerceIn(0f, 1f)
    val collapsedBarHeight = with(LocalDensity.current) { collapsedBarHeightPx.toDp() }
    Box(
        modifier = modifier.background(color = MercadoPagoTheme.color.background.primary),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = MercadoPagoTheme.spacing.paddings.xsmall),
        ) {
            Spacer(modifier = Modifier.height(collapsedBarHeight))
            MPHeaderExpandedTitle(
                title = title,
                subtitle = subtitle,
                onHeightMeasured = { titleBlockHeightPx = it },
            )
            content.invoke()
        }
        MPHeaderCollapsedBar(
            title = title,
            titleOpacity = inlineTitleOpacity,
            isScrolled = scrollState.value > 0,
            onBackClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .onGloballyPositioned { coordinates ->
                    collapsedBarHeightPx = coordinates.size.height.toFloat()
                },
        )
    }
}

@Composable
private fun MPHeaderCollapsedBar(
    title: String,
    titleOpacity: Float,
    isScrolled: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val defaults = getMPHeaderDefaults()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = if (isScrolled) defaults.colors.fillOnScroll else Color.Transparent)
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                vertical = MercadoPagoTheme.spacing.paddings.micro,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeaderBackButton(onClick = onBackClick)
        MPHeaderCollapsedTitle(
            title = title,
            opacity = titleOpacity,
            modifier = Modifier.weight(1f),
        )
        // Mirrors the back button width so the title stays centered in the bar.
        Spacer(modifier = Modifier.size(MercadoPagoTheme.spacing.gap.medium))
    }
}

@Composable
private fun MPHeaderExpandedTitle(
    title: String,
    subtitle: String,
    onHeightMeasured: (Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                vertical = MercadoPagoTheme.spacing.paddings.xmicro,
            )
            .onGloballyPositioned { coordinates ->
                onHeightMeasured(coordinates.size.height.toFloat())
            },
    ) {
        MPText(
            text = title,
            style = MercadoPagoTheme.typography.heading.default.huge,
        )
        if (subtitle.isNotBlank()) {
            Spacer(modifier = Modifier.size(MercadoPagoTheme.spacing.paddings.xmicro))
            MPText(
                text = subtitle,
                style = MercadoPagoTheme.typography.body.default.medium,
            )
        }
    }
}

@Composable
private fun MPHeaderCollapsedTitle(
    title: String,
    opacity: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        MPText(
            text = title,
            style = MercadoPagoTheme.typography.heading.default.small,
            modifier = Modifier.alpha(opacity),
        )
    }
}

private const val INLINE_TITLE_THRESHOLD = 0.75f

@Composable
private fun HeaderBackButton(
    modifier: Modifier = Modifier,
    defaults: MPHeaderDefaults = getMPHeaderDefaults(),
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(MercadoPagoTheme.spacing.gap.medium)
            .clip(MercadoPagoTheme.shape.medium)
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

@Composable
private fun MPHeaderScrollOffPreviewContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        repeat(Int.SIZE_BITS) { index ->
            MPText(
                text = "Item $index",
                style = MercadoPagoTheme.typography.body.default.medium,
                color = MercadoPagoTheme.color.text.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}

@Composable
private fun MPHeaderScrollOnPreviewContent() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(Int.SIZE_BITS) { index ->
            MPText(
                text = "Item $index",
                style = MercadoPagoTheme.typography.body.default.medium,
                color = MercadoPagoTheme.color.text.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}

@Preview(name = "MPHeader ScrollOff", group = "HEADER")
@Composable
private fun MPHeaderScrollOffPreview() {
    MercadoPagoTheme {
        MPHeader(
            title = "Page Title",
            subtitle = "Label",
            onBackClick = {},
            content = { MPHeaderScrollOffPreviewContent() },
        )
    }
}

@Preview(name = "MPHeader ScrollOn", group = "HEADER")
@Composable
private fun MPHeaderScrollOnPreview() {
    MercadoPagoTheme {
        MPHeader(
            title = "Page Title",
            onBackClick = {},
            content = { MPHeaderScrollOnPreviewContent() },
        )
    }
}
