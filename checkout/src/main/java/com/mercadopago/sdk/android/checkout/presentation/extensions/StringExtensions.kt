package com.mercadopago.sdk.android.checkout.presentation.extensions

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import java.text.NumberFormat
import java.util.Locale

internal fun Locale?.getCurrencyString(): String {
    val locale = this ?: Locale.getDefault()
    return NumberFormat.getCurrencyInstance(locale).currency?.symbol.orEmpty()
}

internal fun String.hasAllSameDigits(): Boolean {
    val digits = this.filter { it.isDigit() }
    return digits.isNotEmpty() && digits.all { it == digits.first() }
}

internal fun String.isBeingCleared(
    previousValue: String,
): Boolean = this.length < previousValue.length

internal fun String.toCardTypeErrorMessage(
    stringProvider: StringProvider,
): String {
    val baseMessage = stringProvider.getString(R.string.card_form_error_card_type_not_accepted)
    val cardTypeStringId = when (this) {
        CardType.CREDIT.value -> R.string.card_type_credit_card
        CardType.DEBIT.value -> R.string.card_type_debit_card
        CardType.PREPAID.value -> R.string.card_type_prepaid
        else -> return baseMessage
    }
    return "$baseMessage ${stringProvider.getString(cardTypeStringId)}"
}

internal fun CardBrand.toCardBrandErrorMessage(
    stringProvider: StringProvider,
): String {
    val baseMessage = stringProvider.getString(R.string.card_form_error_card_brand_not_accepted)
    return "$baseMessage $name"
}
