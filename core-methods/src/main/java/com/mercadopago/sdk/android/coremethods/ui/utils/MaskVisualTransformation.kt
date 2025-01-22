package com.mercadopago.sdk.android.coremethods.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntOne
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntZero
import kotlin.math.absoluteValue

/** Creates a visual transformation that masks the input of a field. It helps
 * with many input types like card numbers, dates...
 */
class MaskVisualTransformation(
    private val mask: String,
) : VisualTransformation {

    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = IntZero
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

    private fun offsetTranslator() = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val offsetValue = offset.absoluteValue
            if (offsetValue == IntZero) return IntZero
            var numberOfHashtags = IntZero
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < offsetValue
            }
            return masked.length + IntOne
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == '#' }
        }
    }
}

internal object MaskVisualTransformationDefaults {
    val CardNumber = MaskVisualTransformation("#### #### #### ####")
    val ExpirationDate = MaskVisualTransformation("##/##")
}
