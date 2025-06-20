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

internal const val COMPONENT_NAME_SECURITY_CODE = "securityCode"
internal const val MIN_LENGTH = 3

/**
 * A PCI-compliant security code input component for entering card CVV/CVC codes.
 *
 * This component provides a secure input field for card security codes, with automatic formatting
 * and validation. It supports both 3-digit (CVV) and 4-digit (CVC) security codes, with real-time
 * feedback through events for validation and input state changes.
 *
 * The component integrates with [PCIFieldState] for secure input management and provides events
 * through [SecurityCodeTextFieldEvent] for handling focus changes, input completion, and length changes.
 *
 * Features:
 * - PCI-compliant input handling
 * - Automatic digit validation
 * - Configurable security code length
 * - Real-time input state feedback
 * - Customizable appearance and behavior
 *
 * Example usage:
 * ```kotlin
 * val state = rememberPCIFieldState()
 *
 * SecurityCodeTextField(
 *     state = state,
 *     onEvent = { event ->
 *         when (event) {
 *             is SecurityCodeTextFieldEvent.OnInputFilled -> {
 *                 // Handle input completion
 *             }
 *             is SecurityCodeTextFieldEvent.OnLengthChanged -> {
 *                 // Update progress indicator
 *             }
 *             is SecurityCodeTextFieldEvent.OnFocusChanged -> {
 *                 // Show/hide security code hint
 *             }
 *         }
 *     },
 *     securityCodeSize = 3, // For 3-digit CVV
 *     textStyle = MaterialTheme.typography.bodyLarge,
 *     keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
 * )
 * ```
 *
 * @param modifier Modifier to customize the style and behavior of the field
 * @param state A [PCIFieldState] object that manages the secure input data
 * @param onEvent Callback triggered for field events (focus changes, input completion, length changes)
 * @param securityCodeSize Maximum number of digits allowed (typically 3 or 4)
 * @param enabled Controls whether the field is interactive
 * @param readOnly Controls whether the field is editable
 * @param decorationBox Composable for adding decorative elements around the text field
 * @param textStyle Text style applied to the input content
 * @param keyboardOptions Configuration for the software keyboard
 * @param cursorBrush Brush applied to customize the cursor appearance
 * @param visualTransformation Visual transformations applied to the input text
 *
 * @see PCIFieldState
 * @see SecurityCodeTextFieldEvent
 * @see PCITextField
 */
@Composable
fun SecurityCodeTextField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    onEvent: (SecurityCodeTextFieldEvent) -> Unit,
    securityCodeSize: Int = MIN_LENGTH,
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
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricPCIFieldInitialization(
                field = COMPONENT_NAME_SECURITY_CODE,
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
                        field = COMPONENT_NAME_SECURITY_CODE,
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
        securityCodeSize = MIN_LENGTH,
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
        securityCodeSize = MIN_LENGTH,
    )
}
