package com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode

interface SecurityCodeFieldEvent {
    /**
     * This function informs if the cvv input has been filled.
     * @param isFilled: cvv input is filled or not
     */
    data class Filled(val isFilled: Boolean) : SecurityCodeFieldEvent

    /**
     * This function informs the text length.
     * @param length: number of characters typed
     */
    data class Length(val length: Int) : SecurityCodeFieldEvent

    /**
     * This function informs if the cvv input has been focused.
     * @param isFocused: cvv input is focused or not
     */
    data class FocusChanged(val isFocused: Boolean) : SecurityCodeFieldEvent
}
