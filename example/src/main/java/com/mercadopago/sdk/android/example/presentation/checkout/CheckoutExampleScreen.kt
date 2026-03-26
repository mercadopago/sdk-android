package com.mercadopago.sdk.android.example.presentation.checkout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.checkout.core.MercadoPagoCheckout
import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardFormConfiguration
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.UserCancelledContext
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
internal fun CheckoutExampleScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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
            snackBar = snackbarHostState,
            onOpenCheckoutClicked = {
                checkout.show { result ->
                    val message = when (result) {
                        is MercadoPagoCheckoutResult.Success -> "✅ Pagamento realizado com sucesso!"
                        is MercadoPagoCheckoutResult.Error -> "❌ Erro em ${result.error.errorLocalized} - message: ${result.error.errorMessage}"
                        is MercadoPagoCheckoutResult.UserCancelled -> {
                            val fieldsInfo = when (val cancelContext = result.context) {
                                is UserCancelledContext.CardForm -> {
                                    cancelContext.context.fields.joinToString("\n") { field ->
                                        "${field.field.name}: ${field.state::class.simpleName}"
                                    }
                                }
                            }
                            "⚠️ Checkout cancelado pelo usuário\n\nEstado dos campos:\n$fieldsInfo"
                        }
                        else -> "❌ Erro desconhecido"
                    }
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                }
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun CheckoutExampleScreen(
    snackBar: SnackbarHostState,
    onOpenCheckoutClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBar) },
    ) { paddingValues ->
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
            snackBar = remember { SnackbarHostState() },
            onOpenCheckoutClicked = { },
        )
    }
}
