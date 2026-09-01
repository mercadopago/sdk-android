package com.mercadopago.sdk.android.checkout.presentation.shared

internal data class FooterState(
    val title: String = "",
    val currencySymbol: String = "",
    val amountIntegerPart: String = "",
    val amountDecimalPart: String = "",
    val subtitle: String? = null,
    val buttonLabel: String? = null,
    val isVisible: Boolean = false,
    val buttonState: ButtonState? = null,
)

internal fun FooterState.withButtonEnabled(
    enabled: Boolean,
): FooterState = copy(buttonState = (buttonState ?: ButtonState()).copy(enabled = enabled))

internal fun FooterState.withButtonLoading(
    isLoading: Boolean,
): FooterState = copy(buttonState = (buttonState ?: ButtonState()).copy(isLoading = isLoading))
