package com.mercadopago.sdk.android.coremethods.domain.usecase.validations

internal class IsSecurityCodeValidUseCase {

    operator fun invoke(
        securityCodeSize: Int,
        securityCodeLength: Int
    ): Boolean {

        return true
    }
}
