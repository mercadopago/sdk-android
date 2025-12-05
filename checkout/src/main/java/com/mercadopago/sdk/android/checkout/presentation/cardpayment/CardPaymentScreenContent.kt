package com.mercadopago.sdk.android.checkout.presentation.cardpayment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberTextFieldState
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.components.MPCardNumberTextField
import com.mercadopago.sdk.android.components.MPExpirationDateTextField
import com.mercadopago.sdk.android.components.MPIdentificationTextField
import com.mercadopago.sdk.android.components.MPSecurityCodeTextField
import com.mercadopago.sdk.android.components.MPSimpleTextField
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun CardPaymentScreenContent(
    expirationDateState: ExpirationDateState,
    secureCodeState: SecurityCodeState,
    cardNumberState: CardNumberTextFieldState,
    cardHolderState: CardHolderState,
    identificationTypeState: IdentificationTypeState
) {
    val cardNumberPCIState = rememberPCIFieldState()
    val expirationDatePCIState = rememberPCIFieldState()
    val securityCodePCIState = rememberPCIFieldState()

    Column {
        MPCardNumberTextField(
            modifier = Modifier
                .fillMaxWidth(),
            state = cardNumberPCIState,
            isFocused = cardNumberState.isFocused,
            showPlaceHolder = cardNumberState.showPlaceHolder,
            error = cardNumberState.error.first,
            enabled = cardNumberState.enabled,
            label = cardNumberState.label,
            helper = cardNumberState.helper,
            placeHolder = cardNumberState.placeHolder,
        ) { }

        if (cardHolderState.show) {
            val cardHolderStatePCIState = rememberPCIFieldState()
            MPSimpleTextField(
                modifier = Modifier.fillMaxWidth(),
                state = cardHolderStatePCIState,
                isFocused = cardHolderState.isFocused,
                showPlaceHolder = cardHolderState.showPlaceHolder,
                error = cardHolderState.error,
                enabled = cardHolderState.enabled,
                label = cardNumberState.label,
                helper = cardNumberState.helper,
                placeHolder = cardNumberState.placeHolder
            ) {

            }
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {

            MPExpirationDateTextField(
                modifier = Modifier.weight(1f),
                state = expirationDatePCIState,
                isFocused = expirationDateState.isFocused,
                showPlaceHolder = expirationDateState.showPlaceHolder,
                error = expirationDateState.error.first,
                enabled = expirationDateState.enabled,
                label = expirationDateState.label,
                helper = expirationDateState.helper,
                placeHolder = expirationDateState.placeHolder
            ) { }

            Spacer(Modifier.width(16.dp))

            MPSecurityCodeTextField(
                modifier = Modifier.weight(1f),
                state = securityCodePCIState,
                securityCodeSize = secureCodeState.secureCodeLength,
                isFocused = secureCodeState.isFocused,
                showPlaceHolder = secureCodeState.showPlaceHolder,
                error = secureCodeState.error.first,
                enabled = secureCodeState.enabled,
                label = secureCodeState.label,
                helper = secureCodeState.helper,
                placeHolder = secureCodeState.placeHolder
            ) {

            }
        }

        if (identificationTypeState.show) {
            identificationTypeState.identificationTypes?.let {
                val identificationPCIState = rememberPCIFieldState()
                MPIdentificationTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = identificationPCIState,
                    identificationTypes = identificationTypeState.identificationTypes,
                    selectedIdentificationType = identificationTypeState.selected,
                    isFocused = identificationTypeState.isFocused,
                    showPlaceHolder = identificationTypeState.showPlaceHolder,
                    error = identificationTypeState.error,
                    enabled = identificationTypeState.enabled,
                    label = identificationTypeState.label,
                    helper = identificationTypeState.helper,
                    placeHolder = identificationTypeState.placeHolder
                ) {

                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Card Payment Screen - Default")
@Composable
private fun CardPaymentScreenContentPreview() {
    MercadoPagoTheme {
        CardPaymentScreenContent(
            expirationDateState = ExpirationDateState(
                label = "Vencimiento",
                placeHolder = "MM/AA"
            ),
            secureCodeState = SecurityCodeState(
                label = "CVV",
                placeHolder = "123",
                secureCodeLength = 3
            ),
            cardNumberState = CardNumberTextFieldState(
                label = "Número de tarjeta",
                placeHolder = "0000 0000 0000 0000"
            ),
            cardHolderState = CardHolderState(
                show = true,
                label = "Nombre del titular",
                placeHolder = "Como figura en la tarjeta"
            ),
            identificationTypeState = IdentificationTypeState(
                show = true,
                label = "Documento",
                placeHolder = "Número de documento",
                identificationTypes = listOf(
                    IdentificationType(
                        id = "CPF",
                        name = "CPF",
                        type = "number",
                        minLength = 11,
                        maxLength = 11
                    ),
                    IdentificationType(
                        id = "CNPJ",
                        name = "CNPJ",
                        type = "number",
                        minLength = 14,
                        maxLength = 14
                    )
                ),
                selected = IdentificationType(
                    id = "CPF",
                    name = "CPF",
                    type = "number",
                    minLength = 11,
                    maxLength = 11
                )
            )
        )
    }
}

@Preview(showBackground = true, name = "Card Payment Screen - Without Card Holder")
@Composable
private fun CardPaymentScreenContentWithoutCardHolderPreview() {
    MercadoPagoTheme {
        CardPaymentScreenContent(
            expirationDateState = ExpirationDateState(
                label = "Vencimiento",
                placeHolder = "MM/AA"
            ),
            secureCodeState = SecurityCodeState(
                label = "CVV",
                placeHolder = "123",
                secureCodeLength = 3
            ),
            cardNumberState = CardNumberTextFieldState(
                label = "Número de tarjeta",
                placeHolder = "0000 0000 0000 0000"
            ),
            cardHolderState = CardHolderState(show = false),
            identificationTypeState = IdentificationTypeState(show = false)
        )
    }
}

@Preview(showBackground = true, name = "Card Payment Screen - With Error")
@Composable
private fun CardPaymentScreenContentWithErrorPreview() {
    MercadoPagoTheme {
        CardPaymentScreenContent(
            expirationDateState = ExpirationDateState(
                label = "Vencimiento",
                placeHolder = "MM/AA",
                error = Pair(true, "Fecha inválida")
            ),
            secureCodeState = SecurityCodeState(
                label = "CVV",
                placeHolder = "123",
                secureCodeLength = 3,
                error = Pair(true, "CVV inválido")
            ),
            cardNumberState = CardNumberTextFieldState(
                label = "Número de tarjeta",
                placeHolder = "0000 0000 0000 0000",
                error = Pair(true, "Número de tarjeta inválido")
            ),
            cardHolderState = CardHolderState(
                show = true,
                label = "Nombre del titular",
                placeHolder = "Como figura en la tarjeta",
                error = true
            ),
            identificationTypeState = IdentificationTypeState(
                show = true,
                label = "Documento",
                placeHolder = "Número de documento",
                error = true,
                identificationTypes = listOf(
                    IdentificationType(
                        id = "CPF",
                        name = "CPF",
                        type = "number",
                        minLength = 11,
                        maxLength = 11
                    )
                ),
                selected = IdentificationType(
                    id = "CPF",
                    name = "CPF",
                    type = "number",
                    minLength = 11,
                    maxLength = 11
                )
            )
        )
    }
}
