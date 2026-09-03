package com.mercadopago.sdk.android.analytics.domain.models

import androidx.annotation.RestrictTo

/** Closed catalog of observable native SDK operations. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
enum class NativeErrorOperation(
    /** Stable wire value sent to the ingestion API. */
    val value: String,
    /** SDK module that owns the operation. */
    val module: NativeErrorModule,
    /** Allowlisted downstream service associated with the operation, if any. */
    val serviceTarget: String?,
) {
    /** Fetching identification types. */
    IDENTIFICATION_TYPES("identification_types", NativeErrorModule.CORE_METHODS, "identification_types"),
    /** Fetching installments. */
    INSTALLMENTS("installments", NativeErrorModule.CORE_METHODS, "installments"),
    /** Fetching payment methods. */
    PAYMENT_METHODS("payment_methods", NativeErrorModule.CORE_METHODS, "payment_methods"),
    /** Fetching issuers. */
    ISSUERS("issuers", NativeErrorModule.CORE_METHODS, "issuers"),
    /** Card tokenization, which may include a preliminary lookup. */
    CARD_TOKENIZATION("card_tokenization", NativeErrorModule.CORE_METHODS, null),
    /** Initializing the native card form. */
    CARD_FORM_INITIALIZATION("card_form_initialization", NativeErrorModule.CHECKOUT, "checkout_initialization"),
    /** Submitting the native card form. */
    CARD_FORM_SUBMISSION("card_form_submission", NativeErrorModule.CHECKOUT, null),
    /** Cancelling the native card form. */
    CARD_FORM_CANCELLATION("card_form_cancellation", NativeErrorModule.CHECKOUT, null),
    /** Cancelling installments selection. */
    INSTALLMENTS_CANCELLATION("installments_cancellation", NativeErrorModule.CHECKOUT, null),
    /** Submitting an order. */
    ORDER_SUBMISSION("order_submission", NativeErrorModule.CHECKOUT, "orders"),
}
