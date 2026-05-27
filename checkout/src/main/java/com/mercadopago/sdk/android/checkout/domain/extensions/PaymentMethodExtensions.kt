package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.core.model.MPCardBrand
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig as CheckoutPaymentMethod

private const val DEFAULT_SECURITY_CODE_LENGTH = 3
private const val DEFAULT_SECURITY_CODE_MODE = "mandatory"
private const val SECURITY_CODE_LOCATION = "back"
private const val ISSUER_ID = "issuer_id"

internal fun PaymentMethod.toSecurityCode(): SecurityCode =
    SecurityCode(
        length = card?.securityCode?.length ?: DEFAULT_SECURITY_CODE_LENGTH,
        mode = card?.securityCode?.mode ?: DEFAULT_SECURITY_CODE_MODE,
        location = card?.securityCode?.location ?: SECURITY_CODE_LOCATION,
    )

internal fun PaymentMethod.hasIssuers() =
    this.additionalInfoNeeded?.contains(ISSUER_ID) == true &&
        this.id != null

internal fun List<CheckoutPaymentMethod>?.extractCardFilters(): Pair<List<MPCardType>, List<MPCardBrand>> {
    val cardPayment = this?.filterIsInstance<CheckoutPaymentMethod.Card>()?.firstOrNull()
    return cardPayment?.excludedPaymentTypes.orEmpty() to cardPayment?.excludedPaymentMethods.orEmpty()
}
