package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.IdentificationType as ResponseIdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType as DomainIdentificationType

internal fun ResponseIdentificationType.toDomain(): DomainIdentificationType =
    DomainIdentificationType(
        id = this.id,
        name = this.name,
        type = this.type,
        minLength = this.minLength,
        maxLength = this.maxLength,
        mask = this.mask,
    )
