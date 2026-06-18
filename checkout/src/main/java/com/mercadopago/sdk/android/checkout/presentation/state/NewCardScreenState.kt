package com.mercadopago.sdk.android.checkout.presentation.state

/**
 * Immutable UI state for the new card flow within PaymentBrick.
 *
 * @property isLoading Whether card data is being fetched from `/payment_brick/card`.
 * @property isError Whether the fetch failed.
 * @property cardFormTitle BFF-translated title for the card form screen.
 * @property continueButtonLabel BFF-translated label for the pay/continue button.
 */
internal data class NewCardScreenState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val cardFormTitle: String = "",
    val continueButtonLabel: String = "",
)
