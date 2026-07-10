package com.mercadopago.sdk.android.checkout.presentation.state

internal data class EmailScreenState(
    val title: String = "",
    val buttonLabel: String = "",
    val isButtonEnabled: Boolean = false,
    val fieldState: EmailFieldState = EmailFieldState(),
)

internal data class EmailFieldState(
    override val label: String = "",
    override val helper: String = "",
    override val placeHolder: String = "",
    override val error: String = "",
    override val isFocused: Boolean = false,
    override val filled: Boolean = false,
    override val enabled: Boolean = true,
    override val isValid: Boolean = false,
    override val showPlaceHolder: Boolean = true,
    val value: String = "",
    val validation: ValidationState = ValidationState(),
) : FieldState
