package com.mercadopago.sdk.android.checkout.domain.model

internal val MethodSelectionLayoutType.isArrowLayout: Boolean
    get() = this == MethodSelectionLayoutType.CHEVRON
