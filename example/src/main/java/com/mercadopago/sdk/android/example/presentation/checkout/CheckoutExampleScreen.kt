package com.mercadopago.sdk.android.example.presentation.checkout

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.checkout.core.MercadoPagoCheckout
import com.mercadopago.sdk.android.checkout.core.model.CardFormConfiguration
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme
import java.math.BigDecimal

@Composable
internal fun CheckoutExampleScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val checkout = MercadoPagoCheckout.Builder(
        context = context,
        checkoutType = CheckoutType.CardForm(
            CardFormConfiguration(
                amount = BigDecimal(100.0)
            )
        )
    ).setPaymentMethods(listOf(PaymentMethod.Card()))
        .build()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CheckoutExampleScreen(
            onOpenCheckoutClicked = {
                checkout.show { result ->
                    when (result) {
                        is MercadoPagoCheckoutResult.Success -> {
                            Log.i("CheckoutSuccess", "═══════════════════════════════════════")
                            Log.i("CheckoutSuccess", "Payment completed successfully!")
                            Log.i("CheckoutSuccess", "Token: ${result.paymentData.token}")
                            Log.i("CheckoutSuccess", "Payment Method: ${result.paymentData.paymentMethodId}")
                            Log.i("CheckoutSuccess", "Payment Type: ${result.paymentData.paymentTypeId}")
                            Log.i("CheckoutSuccess", "Amount: ${result.paymentData.transactionAmount}")
                            Log.i("CheckoutSuccess", "Installments: ${result.paymentData.installment ?: "N/A"}")
                            Log.i("CheckoutSuccess", "Issuer ID: ${result.paymentData.issuerId ?: "N/A"}")
                            result.paymentData.payer?.let { payer ->
                                Log.i("CheckoutSuccess", "Payer - Document Type: ${payer.documentType ?: "N/A"}")
                                Log.i("CheckoutSuccess", "Payer - Document Number: ${payer.documentNumber ?: "N/A"}")
                            }
                            Log.i("CheckoutSuccess", "═══════════════════════════════════════")
                        }
                        is MercadoPagoCheckoutResult.Error -> {
                            Log.e("CheckoutError", "═══════════════════════════════════════")
                            Log.e("CheckoutError", "Payment failed!")
                            Log.e("CheckoutError", "Error Type: ${result.error::class.simpleName}")
                            Log.e("CheckoutError", "Error Code: ${result.error.errorCode}")
                            Log.e("CheckoutError", "Error Message: ${result.error.errorMessage}")
                            Log.e("CheckoutError", "Error Localized: ${result.error.errorLocalized}")
                            result.error.errorCause?.let { cause ->
                                Log.e("CheckoutError", "Error Cause: $cause")
                            }
                            Log.e("CheckoutError", "═══════════════════════════════════════")
                        }
                        is MercadoPagoCheckoutResult.UserCancelled -> {
                            Log.w("CheckoutCancelled", "═══════════════════════════════════════")
                            Log.w("CheckoutCancelled", "User cancelled the checkout flow")
                            Log.w("CheckoutCancelled", "═══════════════════════════════════════")
                        }
                    }
                }
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun CheckoutExampleScreen(
    onOpenCheckoutClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Button(
                onClick = onOpenCheckoutClicked
            ) {
                Text(text = "Open Checkout")
            }
        }
    }
}

@Preview
@Composable
fun CheckoutExampleScreenPreview() {
    MercadoPagoSampleTheme {
        CheckoutExampleScreen(
            onOpenCheckoutClicked = { },
        )
    }
}
