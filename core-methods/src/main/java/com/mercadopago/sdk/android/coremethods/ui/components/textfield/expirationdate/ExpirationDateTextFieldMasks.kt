package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.EmptyString
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntFive
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntFour
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntOne
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntTree
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntTwo
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.IntZero

class ExpirationDateTextFieldMasks : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return makeExpirationFilter(text)
    }

    private fun makeExpirationFilter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= IntFour) {
            text.text.substring(IntZero..IntTree)
        } else {
            text.text
        }

        var out = EmptyString
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1) out += "/"
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= IntOne -> offset
                    offset <= IntFour -> offset + IntOne
                    else -> IntFive
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= IntTwo -> offset
                    offset <= IntFive -> offset - IntOne
                    else -> IntFour
                }
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}
