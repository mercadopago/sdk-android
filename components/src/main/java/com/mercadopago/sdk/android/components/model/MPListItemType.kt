package com.mercadopago.sdk.android.components.model

/**
 * List Item type
 */
sealed class MPListItemType {
    /**
     * Radio Button type
     * @param selected Radio Button selected state
     */
    class RadioButton(val selected: Boolean = false) : MPListItemType()

    /**
     * No type
     */
    object None : MPListItemType()
}
