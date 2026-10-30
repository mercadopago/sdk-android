package com.mercadopago.sdk.android.example.presentation.checkout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme

private data class CheckoutFlowItem(
    val type: String,
    val title: String,
    val description: String,
)

private val checkoutFlows = listOf(
    CheckoutFlowItem("card_save", "CardSave", "Cadastra um novo cartão sem cobrar"),
    CheckoutFlowItem("card_transaction", "CardTransaction", "Cobrança com dados do pedido"),
    CheckoutFlowItem("payment", "Payment", "Fluxo de pagamento com cartões salvos"),
)

@Composable
internal fun CheckoutMenuScreen(
    onFlowSelected: (type: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(checkoutFlows, key = { it.type }) { flow ->
            Column(
                modifier = Modifier
                    .clickable { onFlowSelected(flow.type) }
                    .padding(16.dp),
            ) {
                Text(
                    text = flow.title,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = flow.description,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckoutMenuScreenPreview() {
    MercadoPagoSampleTheme {
        CheckoutMenuScreen(onFlowSelected = {})
    }
}
