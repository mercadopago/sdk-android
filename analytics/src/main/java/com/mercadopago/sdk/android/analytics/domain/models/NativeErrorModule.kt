package com.mercadopago.sdk.android.analytics.domain.models

import androidx.annotation.RestrictTo

/** SDK module that originated a native error. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
enum class NativeErrorModule(
    /** Stable wire value sent to the ingestion API. */
    val value: String,
) {
    /** CoreMethods APIs. */
    CORE_METHODS("core_methods"),
    /** Native Checkout flows. */
    CHECKOUT("checkout"),
}
