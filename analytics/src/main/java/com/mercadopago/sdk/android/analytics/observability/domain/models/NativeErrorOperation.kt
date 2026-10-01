package com.mercadopago.sdk.android.analytics.observability.domain.models

import androidx.annotation.RestrictTo

/**
 * Closed catalog of observable native SDK operations.
 *
 * @property value stable wire value sent to the ingestion API.
 * @property module SDK module that owns the operation.
 * @property serviceTarget allowlisted downstream service associated with the operation, if any.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Suppress("UndocumentedPublicProperty")
enum class NativeErrorOperation(
    val value: String,
    val module: NativeErrorModule,
    val serviceTarget: String?,
) {
    IDENTIFICATION_TYPES("identification_types", NativeErrorModule.CORE_METHODS, "identification_types"),
    INSTALLMENTS("installments", NativeErrorModule.CORE_METHODS, "installments"),
    PAYMENT_METHODS("payment_methods", NativeErrorModule.CORE_METHODS, "payment_methods"),
    ISSUERS("issuers", NativeErrorModule.CORE_METHODS, "issuers"),
    /** Card tokenization, which may include a preliminary lookup. */
    CARD_TOKENIZATION("card_tokenization", NativeErrorModule.CORE_METHODS, null),
    CARD_FORM_INITIALIZATION("card_form_initialization", NativeErrorModule.CHECKOUT, "checkout_initialization"),
    CARD_FORM_SUBMISSION("card_form_submission", NativeErrorModule.CHECKOUT, null),
    CARD_FORM_CANCELLATION("card_form_cancellation", NativeErrorModule.CHECKOUT, null),
    INSTALLMENTS_CANCELLATION("installments_cancellation", NativeErrorModule.CHECKOUT, null),
    ORDER_SUBMISSION("order_submission", NativeErrorModule.CHECKOUT, "orders"),
}
