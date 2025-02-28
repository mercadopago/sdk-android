package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.IdentificationTypesResponse
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType

internal fun IdentificationTypesResponse.toModel(): IdentificationType = IdentificationType(
    id = this.id,
    name = this.name,
    type = this.type,
    minLength = this.minLength,
    maxLength = this.maxLength
)
