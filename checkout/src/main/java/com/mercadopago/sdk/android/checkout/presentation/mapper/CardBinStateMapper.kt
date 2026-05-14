package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.extensions.toMask
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentState
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer

internal fun CardPaymentScreenState.applyCardBinData(
    data: CardBinData,
): CardPaymentScreenState =
    copy(
        currencySymbol = data.currencySymbol.orCurrent(currencySymbol),
        cardNumberState = cardNumberState.copy(
            maxLength = data.cardNumber?.config?.length?.max ?: cardNumberState.maxLength,
            mask = (data.cardNumber?.config?.length?.max ?: cardNumberState.maxLength).toMask(),
            image = null,
            label = data.cardNumber?.label.orCurrent(cardNumberState.label),
            placeHolder = data.cardNumber?.placeholder.orCurrent(cardNumberState.placeHolder),
        ),
        secureCodeState = secureCodeState.copy(
            maxLength = data.securityCode?.config?.length?.max ?: secureCodeState.maxLength,
            optional = data.securityCode?.config?.length?.max?.let { it <= 0 } ?: true,
            label = data.securityCode?.label.orCurrent(secureCodeState.label),
            placeHolder = data.securityCode?.placeholder.orCurrent(secureCodeState.placeHolder),
            helper = data.securityCode?.helper.orCurrent(secureCodeState.helper),
            messageTooltip = data.securityCode?.tooltip.orCurrent(secureCodeState.messageTooltip),
        ),
        cardHolderState = cardHolderState.copy(
            label = data.holderName?.label.orCurrent(cardHolderState.label),
            placeHolder = data.holderName?.placeholder.orCurrent(cardHolderState.placeHolder),
            helper = data.holderName?.helper.orCurrent(cardHolderState.helper),
        ),
        expirationDateState = expirationDateState.copy(
            label = data.expirationDate?.label.orCurrent(expirationDateState.label),
            placeHolder = data.expirationDate?.placeholder.orCurrent(expirationDateState.placeHolder),
        ),
        cardIssuers = data.issuers.map { CardIssuer(id = it.id, thumbnail = null) },
        installmentsState = installmentsState.copy(
            showList = data.quotas.isNotEmpty(),
            installments = data.quotas,
            title = data.installmentsTitle.orCurrent(installmentsState.title),
            totalLabel = data.installmentsTotalLabel.orCurrent(installmentsState.totalLabel),
            payButtonLabel = data.installmentsPayButtonLabel.orCurrent(installmentsState.payButtonLabel),
            displayType = data.displayType,
        ),
        paymentState = PaymentState(paymentMethodId = data.id, paymentTypeId = data.paymentTypeId),
    )

private fun String?.orCurrent(
    current: String,
): String = this?.takeIf { it.isNotEmpty() } ?: current
