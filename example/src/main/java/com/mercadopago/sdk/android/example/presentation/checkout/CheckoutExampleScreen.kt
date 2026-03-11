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
        .setCallback { result ->
            when (result) {
                is MercadoPagoCheckoutResult.Success -> {
                    Log.d("CheckoutExample", "Payment successful!")
                    Log.d("CheckoutExample", "Token: ${result.paymentData.token}")
                    Log.d("CheckoutExample", "Amount: ${result.paymentData.transactionAmount}")
                    Log.d("CheckoutExample", "Payment Method: ${result.paymentData.paymentMethodId}")
                    Log.d("CheckoutExample", "Installments: ${result.paymentData.installment}")
                }
                is MercadoPagoCheckoutResult.Error -> {
                    Log.e("CheckoutExample", "Payment error!")
                    Log.e("CheckoutExample", "Error code: ${result.error.serviceError}")
                    Log.e("CheckoutExample", "Error message: ${result.error.message}")
                }
                is MercadoPagoCheckoutResult.UserCancelled -> {
                    Log.i("CheckoutExample", "User cancelled the checkout")
                }
            }
        }
        .build()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CheckoutExampleScreen(
            onOpenCheckoutClicked = checkout::start,
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
