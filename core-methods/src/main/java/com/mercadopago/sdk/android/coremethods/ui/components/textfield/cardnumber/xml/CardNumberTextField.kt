package com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.xml

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
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.DEFAULT_CARD_NUMBER_MAX_LENGTH
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformationDefaults
import kotlin.math.max
import kotlin.math.min

/**
 * A PCI-compliant XML view component for entering credit/debit card numbers.
 *
 * This component provides a secure input field for card numbers with automatic formatting
 * and validation. It supports various card number formats (8-19 digits) and automatically
 * detects card types based on BIN (Bank Identification Number).
 *
 * Features:
 * - PCI-compliant input handling
 * - Automatic card number formatting
 * - BIN detection and card type identification
 * - Real-time validation using Luhn algorithm
 * - Customizable appearance and behavior
 *
 * Example XML usage:
 * ```xml
 * <com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.xml.CardNumberTextField
 *     android:id="@+id/cardNumberField"
 *     android:layout_width="match_parent"
 *     android:layout_height="wrap_content"
 *     app:maxLength="16"
 *     app:readOnly="false"
 *     app:cursorColor="@color/primary" />
 * ```
 *
 * Example Kotlin usage:
 * ```kotlin
 * val cardNumberField = findViewById<CardNumberTextField>(R.id.cardNumberField)
 *
 * // Configure the field
 * cardNumberField.maxLength = 16 // For standard credit cards
 * cardNumberField.textStyle = TextStyle(
 *     color = Color.Black,
 *     fontSize = 16.sp
 * )
 *
 * // Handle events
 * cardNumberField.onEvent = { event ->
 *     when (event) {
 *         is CardNumberTextFieldEvent.OnBinChanged -> {
 *             // Handle BIN change and detect card type
 *             val cardType = detectCardType(event.cardBin)
 *             updateCardIssuerIcon(cardType)
 *         }
 *         is CardNumberTextFieldEvent.IsValid -> {
 *             if (event.isValid) {
 *                 // Enable next step
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * @param context The context in which the view is running
 * @param attrs The attributes of the XML tag that is inflating the view
 * @param defStyle The default style to apply to this view
 */
@Suppress("ktlint:annotation-wrapping")
class CardNumberTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AbstractComposeView(context, attrs, defStyle) {
    /**
     * The state holder that manages the card number input value and PCI compliance.
     * This state is automatically initialized when the view is created and persists
     * across configuration changes.
     */
    lateinit var state: PCIFieldState

    /**
     * Callback triggered for field events (BIN changes, validation, input completion).
     * Use this to handle user interactions and update the UI accordingly.
     */
    var onEvent: (CardNumberTextFieldEvent) -> Unit = { }

    /**
     * Controls whether the field is editable or read-only.
     * When true, the field can be focused but not edited.
     */
    var readOnly: Boolean = false

    /**
     * Text style applied to the card number input.
     * Use this to customize the appearance of the input text.
     */
    var textStyle: TextStyle = TextStyle.Default

    /**
     * Brush applied to customize the text cursor appearance.
     * Default is unspecified color.
     */
    var cursorBrush: Brush = SolidColor(Color.Unspecified)

    /**
     * Configuration for the software keyboard.
     * Use this to customize keyboard behavior and appearance.
     */
    var keyboardOption: KeyboardOptions = KeyboardOptions()

    /**
     * Callbacks for keyboard action events.
     * Use this to handle keyboard actions like "Done" or "Next".
     */
    var keyboardActions: KeyboardActions = KeyboardActions()

    /**
     * Visual transformation for formatting the card number display.
     * Default is card number mask with spaces between groups.
     */
    var visualTransformation: VisualTransformation = MaskVisualTransformationDefaults.CardNumber

    /**
     * Maximum length of the card number input.
     * This value is clamped between 8 and 19 digits to ensure valid card number lengths.
     * The actual length may be adjusted after BIN detection.
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
