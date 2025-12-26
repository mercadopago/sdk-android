package com.mercadopago.sdk.android.coremethods.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_ONE
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_ZERO
import kotlin.math.absoluteValue

/**
 * A visual transformation that applies a mask pattern to text input.
 * This class helps format various types of input like card numbers, dates, and phone numbers
 * by inserting special characters at predefined positions while maintaining the original input.
 *
 * The mask pattern uses '#' to represent input characters and any other character as a
 * literal that will be inserted at that position. The transformation preserves the original
 * input value while displaying it with the specified formatting.
 *
 * Example:
 * ```kotlin
 * // Create a mask for a card number (#### #### #### ####)
 * val cardNumberMask = MaskVisualTransformation("#### #### #### ####")
 *
 * // Apply the mask to a text field
 * TextField(
 *     value = text,
 *     onValueChange = { text = it },
 *     visualTransformation = cardNumberMask
 * )
 *
 * // Input: "4111111111111111"
 * // Display: "4111 1111 1111 1111"
 * ```
 *
 * @param mask The pattern to apply, using '#' for input characters and other characters as literals
 */
class MaskVisualTransformation(
    private val mask: String,
) : VisualTransformation {
    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }

    override fun filter(
        text: AnnotatedString,
    ): TransformedText {
        var out = ""
        var maskIndex = INT_ZERO
        text.forEach { char ->
            while (specialSymbolsIndices.contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), offsetTranslator())
    }

    private fun offsetTranslator() =
        object : OffsetMapping {
            override fun originalToTransformed(
                offset: Int,
            ): Int {
                val offsetValue = offset.absoluteValue
                if (offsetValue == INT_ZERO) return INT_ZERO
                var numberOfHashtags = INT_ZERO
                val masked = mask.takeWhile {
                    if (it == '#') numberOfHashtags++
                    numberOfHashtags < offsetValue
                }
                return masked.length + INT_ONE
            }

            override fun transformedToOriginal(
                offset: Int,
            ): Int {
                return mask.take(offset.absoluteValue).count { it == '#' }
            }
        }
}

/**
 * Default mask patterns for common input types.
 * This object provides pre-defined mask patterns for frequently used input formats
 * in the payment form.
 */
object MaskVisualTransformationDefaults {
    /**
     * Default mask for card numbers (#### #### #### ####).
     * This mask formats card numbers with spaces after every 4 digits.
     */
    val CardNumber = MaskVisualTransformation("#### #### #### ####")
}
