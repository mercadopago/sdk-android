package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

interface ExpirationDateFieldEvent {
    /**
     * This function informs if the expiration date field has been focused.
     * @param isFocused: expiration date field is focused or not
     */
    data class OnFocusChanged(val isFocused: Boolean) : ExpirationDateFieldEvent

    /**
     * This function informs if the expiration date field has been filled.
     * @param isFilled: expiration date field is filled or not
     */
    data class OnInputFilled(val isFilled: Boolean) : ExpirationDateFieldEvent

    /**
     * This function informs the expiration date field length.
     * @param length: number of characters typed
     */
    data class OnLengthChanged(val length: Int) : ExpirationDateFieldEvent
}
