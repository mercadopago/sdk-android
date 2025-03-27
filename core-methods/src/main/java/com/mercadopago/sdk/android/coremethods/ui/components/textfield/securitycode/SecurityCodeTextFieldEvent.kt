package com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode

/**
 * SecurityCodeTextFieldEvent is a sealed class that represents
 * the events that can be triggered by the security code text field.
 */
interface SecurityCodeTextFieldEvent {
    /**
     * This function informs if the security code field has been filled.
     * @param isFilled: security code field is filled or not
     */
    data class OnInputFilled(val isFilled: Boolean) : SecurityCodeTextFieldEvent

    /**
     * This function informs the security code field length.
     * @param length: number of characters typed
     */
    data class OnLengthChanged(val length: Int) : SecurityCodeTextFieldEvent

    /**
     * This function informs if the security code field has been focused.
     * @param isFocused: security code field is focused or not
     */
    data class OnFocusChanged(val isFocused: Boolean) : SecurityCodeTextFieldEvent
}
