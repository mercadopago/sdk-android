package com.mercadopago.sdk.android.components.model

import com.mercadopago.sdk.android.components.MPBottomSheet

/**
 * A document type option for [MPBottomSheet].
 *
 * @param label Display text shown in the list row; drives selection state
 */
data class MPBottomSheetListItem(
    val label: String,
)
