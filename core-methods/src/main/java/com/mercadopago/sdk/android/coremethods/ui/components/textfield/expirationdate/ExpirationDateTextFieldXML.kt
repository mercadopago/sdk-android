package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

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
import com.mercadopago.sdk.android.coremethods.R
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState

/**
 * Expiration date input XML component wrapper in xml.
 *
 * This component allows users to enter a card expiration date.
 * It integrates the [PCIFieldState] that manages the entry and provides information of state of the field.
 * @param context xml context
 * @param attrs [AttributeSet] for this view
 * @param defStyle def style
 */
class ExpirationDateTextFieldXML
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AbstractComposeView(context, attrs, defStyle) {
    /**
     * A [PCIFieldState] object that contains and manages the input data for the expiration date
     */
    lateinit var state: PCIFieldState

    /**
     * A callback triggered in response to field events, such as focus changes or value changes
     */
    var onEvent: (ExpirationDateTextFieldEvent) -> Unit = {}

    /**
     * Controls whether the field is editable or read-only
     */
    var readOnly: Boolean = false

    /**
     * Text style to be applied to the field's content
     */
    var textStyle: TextStyle = TextStyle.Default

    /**
     * Brush applied to the text field's cursor, allowing customization of the cursor's appearance
     */
    var cursorBrush: Brush = SolidColor(Color.Unspecified)

    /**
     * The keyboard options to be applied to the field
     */
    var keyboardOption: KeyboardOptions = KeyboardOptions()

    /**
     * This changes the max length that the input handle, using the [ExpirationDateFormat] enum class
     * this have to be align to the visual transformation mask
     */
    var dateFormat: ExpirationDateFormat = ExpirationDateFormat.ShortFormat

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MPExpirationDateTextFieldXML,
            0,
            0,
        ).apply {
            try {
                readOnly = getBoolean(R.styleable.MPExpirationDateTextFieldXML_readOnly, false)
                val cursorColor = getColor(
                    R.styleable.MPExpirationDateTextFieldXML_cursorColor,
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
        ExpirationDateTextField(
            state = state,
            onEvent = onEvent,
            enabled = isEnabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOption,
            cursorBrush = cursorBrush,
            dateFormat = dateFormat,
        )
    }
}
