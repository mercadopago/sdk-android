package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.core.utils.KoverIgnore

@KoverIgnore("in development")
internal fun MPCardType.toAnalyticsString(): String =
    when (this) {
        MPCardType.CREDIT -> "credit"
        MPCardType.DEBIT -> "debit"
        MPCardType.PREPAID -> "prepaid"
    }
