package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.xml

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
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateFormat
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState

/**
 * A PCI-compliant XML view component for entering card expiration dates.
 *
 * This component provides a secure input field for card expiration dates with automatic formatting
 * and validation. It supports both short (MM/YY) and long (MM/YYYY) date formats, with real-time
 * feedback through events for validation and input state changes.
 *
 * Features:
 * - PCI-compliant input handling
 * - Automatic date formatting
 * - Support for short and long date formats
 * - Real-time validation
 * - Customizable appearance
 *
 * Example XML usage:
 * ```xml
 * <com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.xml.ExpirationDateTextField
 *     android:id="@+id/expirationDateField"
 *     android:layout_width="match_parent"
 *     android:layout_height="wrap_content"
 *     app:readOnly="false"
 *     app:cursorColor="@color/primary" />
 * ```
 *
 * Example Kotlin usage:
 * ```kotlin
 * val expirationDateField = findViewById<ExpirationDateTextField>(R.id.expirationDateField)
 *
 * // Configure the field
 * expirationDateField.dateFormat = ExpirationDateFormat.ShortFormat
 * expirationDateField.textStyle = TextStyle(
 *     color = Color.Black,
 *     fontSize = 16.sp
 * )
 *
 * // Handle events
 * expirationDateField.onEvent = { event ->
 *     when (event) {
 *         is ExpirationDateTextFieldEvent.IsValid -> {
 *             if (event.isValid) {
 *                 // Enable next step
 *             }
 *         }
 *         is ExpirationDateTextFieldEvent.OnInputFilled -> {
 *             if (event.isFilled) {
 *                 // Handle complete input
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
@Suppress("ktlint:standard:annotation")
class ExpirationDateTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AbstractComposeView(context, attrs, defStyle) {
    /**
     * The state holder that manages the expiration date input value and PCI compliance.
     * This state is automatically initialized when the view is created and persists
     * across configuration changes.
     */
    lateinit var state: PCIFieldState

    /**
     * Callback triggered for field events (focus changes, input completion, validation).
     * Use this to handle user interactions and update the UI accordingly.
     */
    var onEvent: (ExpirationDateTextFieldEvent) -> Unit = {}

    /**
     * Controls whether the field is editable or read-only.
     * When true, the field can be focused but not edited.
     */
    var readOnly: Boolean = false

    /**
     * Text style applied to the expiration date input.
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
     * The format to use for the expiration date input.
     * This determines the input length and display format:
     * - ShortFormat: MM/YY (4 digits)
     * - LongFormat: MM/YYYY (6 digits)
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
