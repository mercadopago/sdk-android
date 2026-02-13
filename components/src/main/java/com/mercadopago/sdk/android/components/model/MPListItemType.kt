package com.mercadopago.sdk.android.components.model

sealed class MPListItemType {
    class RadioButton(val selected: Boolean = false) : MPListItemType()
    object None : MPListItemType()
}
