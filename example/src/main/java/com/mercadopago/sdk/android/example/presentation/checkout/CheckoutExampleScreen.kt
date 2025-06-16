package com.mercadopago.sdk.android.example.presentation.checkout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.checkout.presentation.controller.rememberCheckout
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme

@Composable
internal fun CheckoutExampleScreen(
    modifier: Modifier = Modifier,
) {
    val checkout = rememberCheckout()

    CheckoutExampleScreen(
        onOpenCheckoutClicked = checkout::launchBottomSheet,
        modifier = modifier,
    )
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
