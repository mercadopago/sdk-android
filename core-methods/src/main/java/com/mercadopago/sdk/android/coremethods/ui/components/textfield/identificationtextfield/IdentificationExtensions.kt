package com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield

import androidx.compose.ui.text.input.VisualTransformation
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformation

internal fun IdentificationType?.getVisualTransformation(): VisualTransformation {
    val mask = this?.mask
    return if (mask.isNullOrEmpty()) VisualTransformation.None else MaskVisualTransformation(mask)
}
