package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.extensions.toMask
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentState
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost

internal fun CardPaymentScreenState.applyCardBinData(
    data: CardBinData,
): CardPaymentScreenState =
    copy(
        cardNumberState = buildCardNumberState(data),
        secureCodeState = buildSecureCodeState(data),
        cardHolderState = buildCardHolderState(data),
        expirationDateState = buildExpirationDateState(data),
        cardIssuers = data.issuers.map {
            CardIssuer(
                id = it.id?.toString(),
                thumbnail = it.secureThumbnail,
            )
        },
        installmentsState = buildBinInstallmentsState(data),
        paymentState = PaymentState(paymentMethodId = data.id, paymentTypeId = data.paymentTypeId),
    )

private fun String?.orCurrent(
    current: String,
): String = this?.takeIf { it.isNotEmpty() } ?: current

private fun CardPaymentScreenState.buildCardNumberState(
    data: CardBinData,
) = cardNumberState.copy(
    maxLength = data.cardNumber?.length ?: cardNumberState.maxLength,
    mask = (data.cardNumber?.length ?: cardNumberState.maxLength).toMask(),
    image = data.issuers.firstOrNull()?.secureThumbnail,
    label = data.translations?.cardNumber?.label.orCurrent(cardNumberState.label),
    placeHolder = data.translations?.cardNumber?.placeholder.orCurrent(cardNumberState.placeHolder),
    helper = data.translations?.cardNumber?.helper.orCurrent(cardNumberState.helper),
)

private fun CardPaymentScreenState.buildSecureCodeState(
    data: CardBinData,
) = secureCodeState.copy(
    maxLength = data.securityCode?.length ?: secureCodeState.maxLength,
    optional = data.securityCode?.mode?.equals("optional", ignoreCase = true) == true,
    label = data.translations?.securityCode?.label.orCurrent(secureCodeState.label),
    placeHolder = data.securityCode?.placeholder
        .orCurrent(data.translations?.securityCode?.placeholder.orCurrent(secureCodeState.placeHolder)),
    helper = data.translations?.securityCode?.helper.orCurrent(secureCodeState.helper),
    messageTooltip = data.securityCode?.tooltip
        .orCurrent(data.translations?.securityCode?.tooltip.orCurrent(secureCodeState.messageTooltip)),
)

private fun CardPaymentScreenState.buildCardHolderState(
    data: CardBinData,
) = cardHolderState.copy(
    label = data.translations?.cardHolderName?.label.orCurrent(cardHolderState.label),
    placeHolder = data.translations?.cardHolderName?.placeholder.orCurrent(cardHolderState.placeHolder),
    helper = data.translations?.cardHolderName?.helper.orCurrent(cardHolderState.helper),
)

private fun CardPaymentScreenState.buildExpirationDateState(
    data: CardBinData,
) = expirationDateState.copy(
    label = data.translations?.expirationDate?.label.orCurrent(expirationDateState.label),
    placeHolder = data.translations?.expirationDate?.placeholder.orCurrent(expirationDateState.placeHolder),
    helper = data.translations?.expirationDate?.helper.orCurrent(expirationDateState.helper),
)

private fun CardPaymentScreenState.buildBinInstallmentsState(
    data: CardBinData,
) = installmentsState.copy(
    showList = data.quotas.isNotEmpty(),
    installments = data.quotas.map {
        PayerCost(
            instalments = it.quantity,
            installmentAmount = it.installmentAmount?.toFloatOrNull(),
            totalAmount = it.totalAmount?.toFloatOrNull(),
            discountRate = it.discountRate?.toFloat(),
            labels = listOfNotNull(it.label),
        )
    },
)
