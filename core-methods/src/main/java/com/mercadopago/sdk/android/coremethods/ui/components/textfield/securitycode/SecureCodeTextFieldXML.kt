package com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.mercadopago.sdk.android.coremethods.R
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState

/**
 * Security code input XML component.
 *
 * @property state A [PCIFieldState] object that contains and manages the input data for the security field.
 * @property onEvent A callback triggered in response to field events, such as focus changes or value changes.
 * @property securityCodeSize Length limit for the security code to be entered (default is 3).
 * @property readOnly Controls whether the field is editable or read-only.
 * @property textStyle Text style to be applied to the field's content.
 * @property keyboardOptions Keyboard options that influence the behavior of the input field.
 * @property cursorBrush Brush applied to the text field's cursor, allowing customization of the cursor's appearance.
 * @property visualTransformation Allows for visual transformations to be applied to the text.
 */
class SecureCodeTextFieldXML @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AbstractComposeView(context, attrs, defStyle) {
    lateinit var state: PCIFieldState
    var onEvent: (SecurityCodeTextFieldEvent) -> Unit = {}
    var securityCodeSize: Int = MIN_LENGTH
    var readOnly: Boolean = false
    var textStyle: TextStyle = TextStyle.Default
    var keyboardOptions: KeyboardOptions = KeyboardOptions()
    var cursorBrush: Brush = SolidColor(Color.Unspecified)
    var visualTransformation: VisualTransformation = VisualTransformation.None

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MPSecurityFieldTextFieldXML,
            0,
            0,
        ).apply {
            try {
                securityCodeSize = getInteger(
                    R.styleable.MPSecurityFieldTextFieldXML_securityCodeSize,
                    MIN_LENGTH,
                )

                readOnly = getBoolean(R.styleable.MPSecurityFieldTextFieldXML_readOnly, false)
                val cursorColor = getColor(
                    R.styleable.MPSecurityFieldTextFieldXML_cursorColor,
                    Color.Unspecified.toArgb(),
                )
                cursorBrush = SolidColor(Color(cursorColor))
            } finally {
                recycle()
            }
        }
    }

    @Composable
    override fun Content() {
        state = rememberPCIFieldState()
        SecurityCodeTextField(
            state = state,
            onEvent = onEvent,
            securityCodeSize = securityCodeSize,
            enabled = isEnabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            cursorBrush = cursorBrush,
            visualTransformation = visualTransformation,
        )
    }
}
