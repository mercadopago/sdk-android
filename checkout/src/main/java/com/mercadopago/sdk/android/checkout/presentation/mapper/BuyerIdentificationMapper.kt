package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification

internal fun CardPaymentScreenState.toBuyerIdentification(): BuyerIdentification =
    BuyerIdentification(
        name = cardHolderState.value,
        number = identificationTypeState.value,
        type = identificationTypeState.selected?.name,
    )
