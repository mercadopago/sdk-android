package com.mercadopago.sdk.android

import android.app.Application
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CoreMethods.initialize(publicKey = "TEST-e4bcdfb3-7a4f-45f6-9cbf-625428f2fcec")
    }
}
