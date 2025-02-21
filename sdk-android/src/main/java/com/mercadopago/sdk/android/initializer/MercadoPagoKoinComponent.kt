package com.mercadopago.sdk.android.initializer

import androidx.annotation.RestrictTo
import org.koin.core.Koin
import org.koin.core.component.KoinComponent

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface MercadoPagoKoinComponent : KoinComponent {

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun getKoin(): Koin = MercadoPagoSDK.getInstance().koin
}
