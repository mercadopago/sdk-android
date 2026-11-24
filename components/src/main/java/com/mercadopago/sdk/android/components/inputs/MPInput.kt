package com.mercadopago.sdk.android.components.inputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import com.mercadopago.sdk.android.components.MPHelper
import com.mercadopago.sdk.android.components.MPHelperHierarchy
import com.mercadopago.sdk.android.components.MPHelperType
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.R
import com.mercadopago.sdk.android.components.extensions.addBorder
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun MPInputDecorationBox(
    isFocused: Boolean,
    error: Boolean,
    enabled: Boolean = true,
    defaults: MPInputDefaults,
    content: @Composable (RowScope.() -> Unit),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .addBorder(
                isFocused = isFocused,
                error = error,
                enabled = enabled,
                defaults = defaults,
            )
            .height(MercadoPagoTheme.spacing.paddings.large)
            .padding(horizontal = defaults.spacing.horizontalPadding),
    ) {
        content()
    }
}

@Composable
private fun MPInputTooltipIcon(
    onClickTooltip: () -> Unit,
    tooltip: (@Composable () -> Unit)?,
) {
    val iconSize = MercadoPagoTheme.radius.xlarge
    Box(modifier = Modifier.size(iconSize)) {
        Icon(
            painter = painterResource(R.drawable.ic_tooltip),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClickTooltip.invoke() },
            tint = MercadoPagoTheme.color.icon.accent,
        )
        tooltip?.let { tooltipContent ->
            Box(
                modifier = Modifier.layout { measurable, _ ->
                    val placeable = measurable.measure(Constraints())
                    layout(0, 0) {
                        placeable.place(
                            x = (iconSize.roundToPx() - placeable.width) / 2,
                            y = -placeable.height,
                        )
                    }
                },
            ) {
                tooltipContent()
            }
        }
    }
}

@Composable
internal fun MPInputBody(
    modifier: Modifier = Modifier,
    state: InputLabelState = InputLabelState.Idle,
    label: String = "",
    helper: String = "",
    error: String = "",
    showTooltipIcon: Boolean = false,
    onClickTooltip: () -> Unit = {},
    tooltip: (@Composable () -> Unit)? = null,
    showHelperIcon: Boolean = false,
    defaults: MPInputDefaults,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        if (label.isNotBlank()) {
            Row {
                MPText(
                    text = label,
                    modifier = Modifier.padding(bottom = defaults.spacing.labelPadding),
                    style = MercadoPagoTheme.typography.body.default.medium,
                    color = defaults.colors.textPrimary,
                )
                if (showTooltipIcon) {
                    Spacer(modifier = Modifier.size(MercadoPagoTheme.spacing.paddings.xnano))
                    MPInputTooltipIcon(onClickTooltip = onClickTooltip, tooltip = tooltip)
                }
            }
        }
        content()
        when {
            error.isNotEmpty() -> {
                Spacer(modifier = Modifier.size(MercadoPagoTheme.spacing.paddings.xnano))
                MPInputMessage(
                    text = error,
                    state = InputLabelState.Error,
                    showHelperIcon = true,
                )
            }

            helper.isNotEmpty() -> {
                Spacer(modifier = Modifier.size(MercadoPagoTheme.spacing.paddings.xnano))
                MPInputMessage(
                    text = helper,
                    state = state,
                    showHelperIcon = showHelperIcon,
                )
            }
        }
    }
}

@Composable
internal fun MPInputMessage(
    text: String,
    state: InputLabelState,
    showHelperIcon: Boolean = true,
) {
    when (state) {
        InputLabelState.Idle -> {
            MPHelper(
                text = text,
                showIcon = false,
                hierarchy = MPHelperHierarchy.Quiet,
            )
        }

        InputLabelState.Error -> {
            MPHelper(
                text = text,
                showIcon = showHelperIcon,
                type = MPHelperType.Negative,
                hierarchy = MPHelperHierarchy.Loud,
            )
        }

        InputLabelState.Caution -> {
            MPHelper(
                text = text,
                showIcon = showHelperIcon,
                type = MPHelperType.Caution,
                hierarchy = MPHelperHierarchy.Quiet,
            )
        }

        InputLabelState.ReadOnly -> {}

        InputLabelState.Disabled -> {}
    }
}

@Preview
@Composable
private fun MPInputBodyPreview() {
    val defaults = getMPInputDefaults()
    MPInputBody(
        label = "label text",
        helper = "helper text",
        defaults = defaults,
    ) {
        MPText("Text")
    }
}

internal enum class InputLabelState {
    Idle,
    Caution,
    Disabled,
    Error,
    ReadOnly,
}
