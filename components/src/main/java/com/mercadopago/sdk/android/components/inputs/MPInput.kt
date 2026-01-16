package com.mercadopago.sdk.android.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.components.MPHelper
import com.mercadopago.sdk.android.components.MPHelperHierarchy
import com.mercadopago.sdk.android.components.MPHelperType
import com.mercadopago.sdk.android.components.MPText
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
    state: InputLabelState = InputLabelState.Idle,
    label: String? = null,
    helper: String? = null,
    showHelperIcon: Boolean = false,
    defaults: MPInputDefaults,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        label?.let {
            MPText(
                text = it,
                modifier = Modifier.padding(start = defaults.spacing.labelPadding),
                style = MercadoPagoAndesTheme.typography.title.title,
                color = defaults.colors.textPrimary,
            )
        }
        content()
        helper?.let {
            MPInputMessage(
                it,
                state,
                showHelperIcon,
                defaults,
            )
        }
    }
}

@Composable
internal fun MPInputMessage(
    text: String,
    state: InputLabelState,
    showHelperIcon: Boolean,
    defaults: MPInputDefaults,
) {
    when (state) {
        InputLabelState.Idle -> {
            MPHelper(
                text = text,
                modifier = Modifier.padding(start = defaults.spacing.helperPadding),
                showIcon = false,
                hierarchy = MPHelperHierarchy.Quiet,
            )
        }

        InputLabelState.Error -> {
            MPHelper(
                text = text,
                modifier = Modifier.padding(start = defaults.spacing.helperPadding),
                showIcon = showHelperIcon,
                type = MPHelperType.Negative,
                hierarchy = MPHelperHierarchy.Loud,
            )
        }

        InputLabelState.Caution -> {
            MPHelper(
                text = text,
                modifier = Modifier.padding(start = defaults.spacing.helperPadding),
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
