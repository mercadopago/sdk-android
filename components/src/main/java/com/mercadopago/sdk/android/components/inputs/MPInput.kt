package com.mercadopago.sdk.android.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.MP_EMPTY_STRING
import com.mercadopago.sdk.android.components.extensions.addBorder
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme

@Composable
internal fun MPInputDecorationBox(
    isFocused: Boolean,
    error: Boolean,
    defaults: MPInputDefaults,
    content: @Composable (RowScope.() -> Unit),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .addBorder(
                isFocused = isFocused,
                error = error,
                defaults = defaults,
            )
            .height(OutlinedTextFieldDefaults.MinHeight)
            .padding(horizontal = defaults.spacing.horizontalPadding),
    ) {
        content()
    }
}

@Composable
internal fun MPInputBody(
    modifier: Modifier = Modifier,
    error: Boolean = false,
    enabled: Boolean = true,
    label: String? = null,
    helper: String? = null,
    showHelperIcon: Boolean = false,
    icon: ImageVector? = null,
    defaults: MPInputDefaults,
    content: @Composable () -> Unit,
) {
    val state =
        if (error) {
            InputLabelState.Error
        } else if (!enabled) {
            InputLabelState.Disabled
        } else {
            InputLabelState.Idle
        }
    Column(modifier = modifier) {
        label?.let {
            MPInputLabel(
                it,
                modifier = Modifier.padding(start = defaults.spacing.labelPadding),
                style = MercadoPagoAndesTheme.typography.body.bodySmallRegular,
                inputLabelState = state,
                defaults = defaults,
            )
        }
        content()
        helper?.let {
            MPInputHelper(
                text = it,
                modifier = Modifier.padding(start = defaults.spacing.helperPadding),
                inputLabelState = state,
                showIcon = showHelperIcon,
                icon = icon,
                defaults = defaults,
            )
        }
    }
}

@Composable
internal fun MPInputHelper(
    text: String,
    modifier: Modifier = Modifier,
    showIcon: Boolean = false,
    inputLabelState: InputLabelState = InputLabelState.Idle,
    icon: ImageVector? = null,
    defaults: MPInputDefaults,
) {
    Row {
        if (showIcon) {
            icon?.let {
                Icon(it, MP_EMPTY_STRING)
                Spacer(modifier = Modifier.padding(start = defaults.spacing.labelPadding))
            }
        }
        MPInputLabel(
            text,
            modifier = modifier,
            style = MercadoPagoAndesTheme.typography.body.bodyExtraSmallSemiBold,
            inputLabelState = inputLabelState,
            defaults = defaults,
        )
    }
}

@Composable
internal fun MPInputLabel(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MercadoPagoAndesTheme.typography.title.title,
    inputLabelState: InputLabelState = InputLabelState.Idle,
    defaults: MPInputDefaults,
) {
    val color = when (inputLabelState) {
        InputLabelState.Idle -> defaults.colors.textPrimary
        InputLabelState.Disabled -> defaults.colors.textDisabled
        InputLabelState.Error -> defaults.colors.textError
    }
    MPText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
    )
}

internal enum class InputLabelState {
    Idle,
    Disabled,
    Error,
}
