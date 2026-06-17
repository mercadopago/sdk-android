package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.domain.model.CVVFieldConfig
import com.mercadopago.sdk.android.checkout.domain.model.CVVScreenData
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput

internal fun SecurityCodeScreenOutput.toCVVScreenData(): CVVScreenData =
    CVVScreenData(
        headerTitle = headerTitle,
        field = field.toCVVFieldConfig(),
        continueButtonLabel = continueButtonLabel,
    )

internal fun SecurityCodeFieldOutput.toCVVFieldConfig(): CVVFieldConfig =
    CVVFieldConfig(
        label = label,
        placeholder = placeholder,
        helper = helper,
    )
