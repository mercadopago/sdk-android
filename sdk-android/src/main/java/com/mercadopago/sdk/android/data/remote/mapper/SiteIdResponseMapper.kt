package com.mercadopago.sdk.android.data.remote.mapper

import com.mercadopago.sdk.android.data.remote.exception.SiteIdNotAvailableException
import com.mercadopago.sdk.android.data.remote.response.SiteIdResponse
import com.mercadopago.sdk.android.domain.model.SiteId

internal fun SiteIdResponse.toDomain() = SiteId(
    siteId = siteId ?: throw SiteIdNotAvailableException(),
)
