package com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.coremethods.analytics.metricPCIFieldFocus
import com.mercadopago.sdk.android.coremethods.analytics.metricPCIFieldInitialization
import com.mercadopago.sdk.android.coremethods.ui.components.PreviewGroup
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextFieldTestTags
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState

/**
 * Security code input component.
 *
 * This component allows users to enter a card security code.
 * It integrates the [PCIFieldState] that manages the entry and provides information of state of the field.
 *
 * @param modifier Modifier to customize the style and behavior of the field.
 * @param state A [PCIFieldState] object that contains and manages the input data for the security field.
 * @param onEvent A callback triggered in response to field events, such as focus changes or value changes.
 * @param securityCodeSize Length limit for the security code to be entered (default is 3).
 * @param enabled Controls the enabled state of the [SecurityCodeTextField], allowing or preventing user interaction.
 * @param readOnly Controls whether the field is editable or read-only.
 * @param decorationBox A composable that allows the addition of decorative elements around the text field.
 * @param textStyle Text style to be applied to the field's content.
 * @param keyboardOptions Keyboard options that influence the behavior of the input field.
 * @param cursorBrush Brush applied to the text field's cursor, allowing customization of the cursor's appearance.
 * @param visualTransformation Allows for visual transformations to be applied to the text, such as masking characters.
 *
 * @sample com.mercadopago.sdk.android.coremethods.ui.components.samples.SecurityCodeFieldBasicSample
 * @sample com.mercadopago.sdk.android.coremethods.ui.components.samples.SecurityCodeFieldDecorationBoxSample
 */
@Composable
fun SecurityCodeTextField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    onEvent: (SecurityCodeTextFieldEvent) -> Unit,
    securityCodeSize: Int = 3,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    decorationBox: @Composable (
        innerTextField: @Composable () -> Unit,
    ) -> Unit = @Composable { innerTextField -> innerTextField() },
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    LaunchedEffect(key1 = true) {
        MPAnalytics.getInstance().trackMetric(
            metricPCIFieldInitialization(
                field = "securityCode"
            ),
        )
    }

    PCITextField(
        value = state.input,
        onValueChange = { value ->
            if (value.length <= securityCodeSize && value.none { !it.isDigit() }) {
                onEvent(SecurityCodeTextFieldEvent.OnInputFilled(isFilled = value.length == securityCodeSize))
                onEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = value.length))
                state.input = value
            }
        },
        onFocusChanged = { isFocused ->
            onEvent(SecurityCodeTextFieldEvent.OnFocusChanged(isFocused))
            if (isFocused) {
                MPAnalytics.getInstance().trackMetric(
                    metricPCIFieldFocus(
                        field = "securityCode"
                    ),
                )
            }
        },
        modifier = modifier.testTag(PCITextFieldTestTags.SecurityCode.tag),
        enabled = enabled,
        readOnly = readOnly,
        decorationBox = decorationBox,
        cursorBrush = cursorBrush,
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        textStyle = textStyle,
        visualTransformation = visualTransformation,
    )
}

@Preview(
    name = "Security Code Field Empty",
    group = PreviewGroup.TEXT_FIELD,
    showBackground = true,
)
@Composable
internal fun SecurityCodeEmptyPreview() {
    val state: PCIFieldState = rememberPCIFieldState()
    SecurityCodeTextField(
        state = state,
        onEvent = { securityCodeFieldEvent ->
        },
        securityCodeSize = 3,
    )
}

@Preview(
    name = "Security Code Field Filled",
    group = PreviewGroup.TEXT_FIELD,
    showBackground = true,
)
@Composable
internal fun SecurityCodeFilledPreview() {
    val state: PCIFieldState = rememberPCIFieldState().apply {
        input = "123"
    }
    SecurityCodeTextField(
        state = state,
        onEvent = { securityCodeFieldEvent ->
        },
        securityCodeSize = 3,
    )
}
