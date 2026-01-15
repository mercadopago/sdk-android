package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

enum class MPHeaderType {
    ScrollOff,
    ScrollOn,
    TittleLeft
}

/**
 * A collapsible header component with motion animation support.
 *
 * @param title The title text displayed in the header.
 * @param modifier Modifier to be applied to the header container.
 * @param subtitle Optional subtitle text displayed below the title when expanded.
 * @param hierarchy The visual hierarchy level of the header.
 * @param showBackButton Whether to display the back navigation button.
 * @param backIcon The icon to use for the back button.
 * @param expandedHeight The height of the header when fully expanded.
 * @param collapsedHeight The height of the header when collapsed.
 * @param backgroundColor Optional background color override.
 * @param onBackClick Callback invoked when the back button is clicked.
 * @param content The content to display below the header.
 */
@Composable
fun MPHeader(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String = "",
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
    headerType: MPHeaderType = MPHeaderType.ScrollOff,
    content: @Composable () -> Unit,
) {
    val defaults = getMPHeaderDefaults()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MercadoPagoAndesTheme.color.background.primary)
    ) {
        when (headerType) {
            MPHeaderType.ScrollOff -> {
                Column (
                    modifier = Modifier.padding(MercadoPagoAndesTheme.spacing.paddings.xtiny)
                ){
                    HeaderBackButton {

                    }
                    Spacer(modifier = Modifier.size(MercadoPagoAndesTheme.spacing.paddings.xmicro))
                    MPText(
                        text = title,
                        style = MercadoPagoAndesTheme.typography.heading.default.huge
                    )
                    Spacer(modifier = Modifier.size(MercadoPagoAndesTheme.spacing.paddings.xmicro))
                    MPText(
                        text = subtitle,
                        style = MercadoPagoAndesTheme.typography.body.default.medium
                    )
                }
            }

            MPHeaderType.ScrollOn -> {
                Row (
                    modifier = Modifier.padding(MercadoPagoAndesTheme.spacing.paddings.xtiny),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    HeaderBackButton {

                    }
                    Spacer(modifier = Modifier.size(MercadoPagoAndesTheme.spacing.paddings.xmicro))
                    MPText(
                        text = title,
                        style = MercadoPagoAndesTheme.typography.heading.default.medium
                    )
                }
            }

            MPHeaderType.TittleLeft -> {
                Column (
                    modifier = Modifier.padding(MercadoPagoAndesTheme.spacing.paddings.xtiny)
                ) {
                    MPText(
                        text = title,
                        style = MercadoPagoAndesTheme.typography.heading.default.medium
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
                items(10) { index ->
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
