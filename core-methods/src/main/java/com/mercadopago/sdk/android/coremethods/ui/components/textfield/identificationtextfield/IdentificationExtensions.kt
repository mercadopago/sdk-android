package com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield

import androidx.compose.ui.text.input.VisualTransformation
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformation

internal fun IdentificationType?.getVisualTransformation(): VisualTransformation {
    val mask = this?.mask
    return if (mask.isNullOrEmpty()) VisualTransformation.None else MaskVisualTransformation(mask)
}

// The BFF sends the type with inconsistent casing (e.g. "Number" for DNI), so the comparison is case-insensitive.
// Anything else, including a missing type, stays text so letters are never filtered out silently.
internal fun IdentificationType?.isNumeric(): Boolean = this?.type.equals("number", ignoreCase = true)
