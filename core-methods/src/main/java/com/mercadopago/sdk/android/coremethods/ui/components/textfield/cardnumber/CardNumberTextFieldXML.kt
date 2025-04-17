package com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.text.KeyboardActions
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
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformationDefaults
import kotlin.math.max
import kotlin.math.min

/**
 * Card Number input XML component wrapper in xml.
 *
 * @param context xml context
 * @param attrs [AttributeSet] for this view
 * @param defStyle def style
 */
class CardNumberTextFieldXML(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AbstractComposeView(context, attrs, defStyle) {
    /**
     * The [PCIFieldState] of the component. It makes the field PCI and holds the card number value.
     */
    lateinit var state: PCIFieldState

    /**
     * Callback for the [CardNumberTextFieldEvent].
     */
    var onEvent: (CardNumberTextFieldEvent) -> Unit = { }

    /**
     * Controls the editable state of the [BasicTextField].
     */
    var readOnly: Boolean = false

    /**
     * Style configuration that applies at character level such as color, font etc.
     */
    var textStyle: TextStyle = TextStyle.Default

    /**
     * [Brush] to paint cursor with.
     */
    var cursorBrush: Brush = SolidColor(Color.Unspecified)

    /**
     * Software keyboard options that contains configuration such as [ImeAction].
     */
    var keyboardOption: KeyboardOptions = KeyboardOptions()

    /**
     * When the input service emits an IME action, the corresponding callback
     */
    var keyboardActions: KeyboardActions = KeyboardActions()

    /**
     * The visual transformation filter for changing the visual representation of the card number.
     */
    var visualTransformation: VisualTransformation = MaskVisualTransformationDefaults.CardNumber

    /**
     * It will ensure the users have the right amount of numbers after the bin is completed.
     */
    var maxLength: Int = DEFAULT_CARD_NUMBER_MAX_LENGTH
        set(value) {
            field = min(max(value, 8), DEFAULT_CARD_NUMBER_MAX_LENGTH)
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MPCardNumberTextFieldXML,
            0,
            0,
        ).apply {
            try {
                maxLength = getInteger(
                    R.styleable.MPCardNumberTextFieldXML_maxLength,
                    DEFAULT_CARD_NUMBER_MAX_LENGTH,
                )
                readOnly = getBoolean(R.styleable.MPCardNumberTextFieldXML_readOnly, false)
                val cursorColor = getColor(
                    R.styleable.MPCardNumberTextFieldXML_cursorColor,
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
        CardNumberTextField(
            state = state,
            onEvent = onEvent,
            maxLength = maxLength,
            enabled = isEnabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOption,
            keyboardActions = keyboardActions,
            cursorBrush = cursorBrush,
            visualTransformation = visualTransformation,
        )
    }
}
