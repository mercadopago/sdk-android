package com.mercadopago.sdk.android.analytics.observability.domain.models

import androidx.annotation.RestrictTo

/**
 * SDK module that originated a native error.
 *
 * @property value stable wire value sent to the ingestion API.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Suppress("UndocumentedPublicProperty")
enum class NativeErrorModule(val value: String) {
    CORE_METHODS("core_methods"),
    CHECKOUT("checkout"),
}
