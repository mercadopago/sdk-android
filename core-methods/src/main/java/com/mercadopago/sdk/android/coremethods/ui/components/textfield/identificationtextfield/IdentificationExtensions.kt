package com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield

import androidx.compose.ui.text.input.VisualTransformation
import com.mercadopago.sdk.android.coremethods.data.remote.response.IdentificationTypesResponse
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformation

internal fun IdentificationTypesResponse?.getMask(): String {
    return when (this?.id) {
        "CPF" -> "###.###.###-##"
        "CNPJ" -> "##.###.###/####-##"
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
