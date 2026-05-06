package com.mercadopago.sdk.android.checkout.presentation.cardpayment

import com.mercadopago.sdk.android.components.model.MPBottomSheetListItem
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType

internal fun IdentificationType.toBottomSheetListItem() = MPBottomSheetListItem(label = name.orEmpty())

internal fun List<IdentificationType>.toBottomSheetItems() = map { it.toBottomSheetListItem() }

internal fun List<IdentificationType>.findByBottomSheetItem(
    item: MPBottomSheetListItem,
) = find { it.name == item.label }
