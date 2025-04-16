package com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
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
 * This PCI handles user input of card numbers.
 * It integrates the [PCIFieldState] that manages the entry and provides information of state of the field.
 *
 * @param state The [PCIFieldState] of the component. It makes the field PCI and holds the card number value.
 * @param onEvent Callback for the [CardNumberTextFieldEvent].
 * @param maxLength It will ensure the users have the right amount of numbers after the bin is completed.
 * @param enabled controls the enabled state of the [BasicTextField].
 * @param readOnly controls the editable state of the [BasicTextField].
 * @param textStyle Style configuration that applies at character level such as color, font etc.
 * @param keyboardOptions software keyboard options that contains configuration such as [ImeAction].
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * @param cursorBrush [Brush] to paint cursor with.
 * @param visualTransformation The visual transformation filter for changing the visual
 * representation of the card number.
 *
 */
class CardNumberTextFieldXML
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
    ) : AbstractComposeView(context, attrs, defStyle) {
        lateinit var state: PCIFieldState

        var onEvent: (CardNumberTextFieldEvent) -> Unit = { }
        var readOnly: Boolean = false
        var textStyle: TextStyle = TextStyle.Default
        var cursorBrush: Brush = SolidColor(Color.Unspecified)
        var keyboardOption: KeyboardOptions = KeyboardOptions()
        var keyboardActions: KeyboardActions = KeyboardActions()
        var visualTransformation: VisualTransformation = MaskVisualTransformationDefaults.CardNumber

        var maxLength: Int = DEFAULT_CARD_NUMBER_MAX_LENGTH
            set(value) {
                field = min(max(value, 8), DEFAULT_CARD_NUMBER_MAX_LENGTH)
            }

        init {
            context.theme.obtainStyledAttributes(attrs, R.styleable.MPCardNumberTextFieldXML, 0, 0)
                .apply {
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
