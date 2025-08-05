package com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.xml

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
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.MIN_LENGTH
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent

/**
 * A PCI-compliant XML view component for entering card security codes (CVV).
 * This component provides a secure input field that handles card security codes with
 * automatic formatting and validation.
 *
 * The component supports customizable security code lengths (typically 3 or 4 digits)
 * and provides real-time feedback through events for validation and input state changes.
 *
 * Example:
 * ```xml
 * <com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.xml.SecurityCodeTextField
 *     android:id="@+id/securityCodeField"
 *     android:layout_width="match_parent"
 *     android:layout_height="wrap_content"
 *     app:securityCodeSize="3"
 *     app:cursorColor="@color/primary"
 *     app:readOnly="false" />
 * ```
 *
 * ```kotlin
 * // Configure the security code field
 * securityCodeField.apply {
 *     onEvent = { event ->
 *         when (event) {
 *             is SecurityCodeTextFieldEvent.OnInputFilled -> {
 *                 if (event.isFilled) {
 *                     // Handle complete input
 *                 }
 *             }
 *         }
 *     }
 *     securityCodeSize = 3
 *     textStyle = TextStyle(
 *         color = Color.Black,
 *         fontSize = 16.sp
 *     )
 * }
 * ```
 *
 * @see SecurityCodeTextFieldEvent
 * @see PCIFieldState
 *
 * @param context The context in which the view is running
 * @param attrs The attributes of the XML tag that is inflating the view
 * @param defStyle The default style to apply to this view
 */
@Suppress("ktlint:standard:annotation")
class SecurityCodeTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AbstractComposeView(context, attrs, defStyle) {
    /**
     * The state holder for the security code input field.
     * This property manages the input value and ensures PCI compliance.
     */
    lateinit var state: PCIFieldState

    /**
     * Callback for handling security code field events.
     * This callback is triggered for various events like focus changes,
     * input completion, and length changes.
     */
    var onEvent: (SecurityCodeTextFieldEvent) -> Unit = {}

    /**
     * The maximum number of digits allowed in the security code.
     * This value determines the length of the security code input
     * (typically 3 for most cards, 4 for American Express).
     */
    var securityCodeSize: Int = MIN_LENGTH

    /**
     * Whether the field is read-only.
     * When true, the field can be focused but not edited,
     * useful for displaying pre-filled values.
     */
    var readOnly: Boolean = false

    /**
     * The text style to be applied to the security code input.
     * This includes properties like color, font size, and font family.
     */
    var textStyle: TextStyle = TextStyle.Default

    /**
     * Configuration for the software keyboard.
     * This includes options like keyboard type and IME actions.
     */
    var keyboardOptions: KeyboardOptions = KeyboardOptions()

    /**
     * The brush used to paint the text cursor.
     * This allows customization of the cursor's appearance.
     */
    var cursorBrush: Brush = SolidColor(Color.Unspecified)

    /**
     * Visual transformation to be applied to the security code input.
     * This can be used to mask or format the input display.
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
