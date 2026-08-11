package com.mercadopago.sdk.android.example.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface SampleDestination {

    @Serializable
    object Home : SampleDestination

    @Serializable
    object SDKInstance : SampleDestination

    @Serializable
    object CoreMethods : SampleDestination

    @Serializable
    object Checkout : SampleDestination

    @Serializable
    data class CheckoutFlow(val type: String) : SampleDestination
}

internal fun SampleDestination.isRoute(route: String?): Boolean {
    return this::class.qualifiedName == route
}
