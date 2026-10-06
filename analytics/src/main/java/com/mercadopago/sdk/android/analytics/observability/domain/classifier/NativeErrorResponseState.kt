package com.mercadopago.sdk.android.analytics.observability.domain.classifier

import androidx.annotation.RestrictTo

/**
 * Closed response-contract evidence.
 *
 * @property value stable canonical value used by shared test vectors.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Suppress("UndocumentedPublicProperty")
enum class NativeErrorResponseState(val value: String) {
    EMPTY_BODY("empty_body"),
    DECODE_FAILURE("decode_failure"),
}
