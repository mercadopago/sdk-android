package com.mercadopago.sdk.android.di

import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import org.koin.core.Koin
import org.koin.core.component.KoinComponent

/**
 * The Koin Component that contains the Koin instance of the SDK.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface MercadoPagoKoinComponent : KoinComponent {

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    override fun getKoin(): Koin = MercadoPagoSDK.Companion.getInstance().koin
}
