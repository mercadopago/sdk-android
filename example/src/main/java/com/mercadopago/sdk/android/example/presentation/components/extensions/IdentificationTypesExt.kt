package com.mercadopago.sdk.android.example.presentation.components.extensions

import androidx.compose.ui.text.input.VisualTransformation
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformation

internal fun IdentificationType?.getPlaceholder(): String {
    return when (this?.id) {
        "CPF" -> "999.999.999-99"
        "CNPJ" -> "99.999.999/9999-99"
        "DI" -> ""
        else -> ""
    }
}

internal fun IdentificationType?.getVisualTransformation(): VisualTransformation {
    return when (this?.id) {
        "CPF" -> MaskVisualTransformation("###.###.###-##")
        "CNPJ" -> MaskVisualTransformation("##.###.###/####-##")
        "DI" -> VisualTransformation.None
        else -> VisualTransformation.None
    }
}
