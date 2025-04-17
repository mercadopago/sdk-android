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
 * @param context xml context
 * @param attrs [AttributeSet] for this view
 * @param defStyle def style
 */
class SecureCodeTextFieldXML @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AbstractComposeView(context, attrs, defStyle) {
    /**
     * A [PCIFieldState] object that contains and manages the input data for the security field.
     */
    lateinit var state: PCIFieldState

    /**
     * A callback triggered in response to field events, such as focus changes or value changes.
     */
    var onEvent: (SecurityCodeTextFieldEvent) -> Unit = {}

    /**
     * Length limit for the security code to be entered (default is 3).
     */
    var securityCodeSize: Int = MIN_LENGTH

    /**
     * Controls whether the field is editable or read-only.
     */
    var readOnly: Boolean = false

    /**
     * Text style to be applied to the field's content.
     */
    var textStyle: TextStyle = TextStyle.Default

    /**
     * Keyboard options that influence the behavior of the input field.
     */
    var keyboardOptions: KeyboardOptions = KeyboardOptions()

    /**
     * Brush applied to the text field's cursor, allowing customization of the cursor's appearance.
     */
    var cursorBrush: Brush = SolidColor(Color.Unspecified)

    /**
     * Allows for visual transformations to be applied to the text, such as masking characters.
     */
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
