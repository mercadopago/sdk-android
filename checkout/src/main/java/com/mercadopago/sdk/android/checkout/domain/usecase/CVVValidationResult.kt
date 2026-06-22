package com.mercadopago.sdk.android.checkout.domain.usecase

internal sealed class CVVValidationResult {
    data object Valid : CVVValidationResult()

    sealed class Invalid : CVVValidationResult() {
        data object Empty : Invalid()

        data object IncorrectLength : Invalid()
    }
}
