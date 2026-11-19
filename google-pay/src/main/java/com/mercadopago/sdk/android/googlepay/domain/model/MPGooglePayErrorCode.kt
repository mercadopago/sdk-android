package com.mercadopago.sdk.android.googlepay.domain.model

import com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayErrorType.PAYMENT
import com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayErrorType.UNAVAILABLE

/**
 * Closed list of codes delivered in [MPGooglePayError.code].
 *
 * @property type type of the error, fixed by the code.
 */
enum class MPGooglePayErrorCode(
    val type: MPGooglePayErrorType,
) {
    /** `MercadoPagoSDK.initialize` was not called before `prepare`. */
    SDK_NOT_INITIALIZED(UNAVAILABLE),

    /** Invalid options (no public key, purchase data or merchant data with a production key). */
    INVALID_CONFIGURATION(UNAVAILABLE),

    /** Device without Google Play Services or with an incompatible version. */
    GOOGLE_PLAY_SERVICES_UNAVAILABLE(UNAVAILABLE),

    /** Google Pay is not enabled for the seller/site. */
    WALLET_NOT_ENABLED(UNAVAILABLE),

    /** No card network left after intersecting Mercado Pago and the options. */
    NO_SUPPORTED_NETWORKS(UNAVAILABLE),

    /** Buyer has no compatible tokenized card in Google Wallet. */
    NO_ELIGIBLE_PAYMENT_METHOD(UNAVAILABLE),

    /** Network error or invalid response from the wallets service. */
    WALLETS_FETCH_FAILED(UNAVAILABLE),

    /** Google Play Services failed on `isReadyToPay`. */
    IS_READY_TO_PAY_FAILED(UNAVAILABLE),

    /** Google Pay rejected the request as a developer error. */
    GOOGLE_PAY_DEVELOPER_ERROR(PAYMENT),

    /** Google Pay internal error. */
    GOOGLE_PAY_INTERNAL_ERROR(PAYMENT),

    /** Any other Google Pay failure on the payment sheet. */
    GOOGLE_PAY_ERROR(PAYMENT),

    /** Card network chosen in the sheet has no Mercado Pago mapping. */
    UNSUPPORTED_CARD_NETWORK(PAYMENT),

    /** Tokenize answered with an error or an invalid body. */
    TOKENIZATION_FAILED(PAYMENT),

    /** Timeout or no connection on tokenize. */
    NETWORK_ERROR(PAYMENT),

    /**
     * The system closed the app during the payment: during tokenize, or with the Google Pay sheet open and
     * no [MPGooglePayResult.Ready] in the `prepare` of the recreated screen. Delivered to that `prepare`.
     *
     * The result of the payment is unknown: check the payment status in your backend. It may arrive even when
     * a previous process already delivered the final result, because the state saved by the system can be
     * older than that result. It never causes a duplicate charge: the interrupted payment is never retried.
     */
    FLOW_INTERRUPTED(PAYMENT),
}
