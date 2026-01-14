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
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.MPTextColorType
import com.mercadopago.sdk.android.components.MPTextStyle
import com.mercadopago.sdk.android.components.MP_EMPTY_STRING
import com.mercadopago.sdk.android.components.extensions.addBorder
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme

@Composable
internal fun MPInputDecorationBox(
    isFocused: Boolean,
    error: Boolean,
    content: @Composable (RowScope.() -> Unit),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .addBorder(
                isFocused = isFocused,
                error = error,
            )
            .height(OutlinedTextFieldDefaults.MinHeight)
            .padding(horizontal = 16.dp),
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
                modifier = Modifier.padding(start = MercadoPagoAndesTheme.spacing.paddings.xnano),
                textStyle = MPTextStyle.BodySmallRegular,
                inputLabelState = state,
            )
        }
        content()
        helper?.let {
            MPInputHelper(
                text = it,
                modifier = Modifier.padding(start = MercadoPagoAndesTheme.spacing.paddings.xnano),
                inputLabelState = state,
                showIcon = showHelperIcon,
                icon = icon,
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
) {
    Row {
        if (showIcon) {
            icon?.let {
                Icon(it, MP_EMPTY_STRING)
                Spacer(modifier = Modifier.padding(start = MercadoPagoAndesTheme.spacing.paddings.xnano))
            }
        }
        MPInputLabel(
            text,
            modifier = modifier,
            inputLabelState = inputLabelState,
            textStyle = MPTextStyle.BodyExtraSmallSemiBold,
        )
    }
}

@Composable
internal fun MPInputLabel(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: MPTextStyle = MPTextStyle.Title,
    inputLabelState: InputLabelState = InputLabelState.Idle,
) {
    val colorType = when (inputLabelState) {
        InputLabelState.Idle -> MPTextColorType.Primary
        InputLabelState.Disabled -> MPTextColorType.Inverted
        InputLabelState.Error -> MPTextColorType.Negative
    }

    MPText(
        text,
        modifier = modifier,
        textStyle = textStyle,
        colorType = colorType,
    )
}

internal enum class InputLabelState {
    Idle,
    Disabled,
    Error,
}
