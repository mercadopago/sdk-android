package com.mercadopago.sdk.android.example.presentation.checkout

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mercadopago.sdk.android.checkout.core.MercadoPagoCheckout
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPPayer
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme
import java.math.BigDecimal

private val ORDER_AMOUNT = BigDecimal("150.00")

private sealed interface CheckoutState {
    data object Idle : CheckoutState
    data class CardSaveSuccess(val token: String) : CheckoutState
    data class CardTransactionSuccess(val orderStatus: String) : CheckoutState
}

@Composable
internal fun CheckoutExampleScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var state by remember { mutableStateOf<CheckoutState>(CheckoutState.Idle) }
    var orderId by remember { mutableStateOf("") }
    var clientToken by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .border(width = 3.dp, color = Color.Red),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "example screen",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
                // Order summary
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Produto de teste",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        )
                        Text(
                            text = "R$ $ORDER_AMOUNT",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "R$ $ORDER_AMOUNT",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                HorizontalDivider()

                // Pay with card section
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                    Text(
                        text = "Pagar com cartão",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                        letterSpacing = 0.5.sp,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        OutlinedTextField(
                            value = orderId,
                            onValueChange = { orderId = it },
                            label = { Text("Order ID", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.labelSmall,
                        )
                        OutlinedTextField(
                            value = clientToken,
                            onValueChange = { clientToken = it },
                            label = { Text("Client Token", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.labelSmall,
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    when (val current = state) {
                        is CheckoutState.CardTransactionSuccess -> {
                            ResultCard(
                                label = "Pagamento concluído",
                                value = current.orderStatus,
                                hint = "status retornado pelo checkout",
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { state = CheckoutState.Idle }) {
                                Text("Novo pagamento")
                            }
                        }
                        else -> {
                            Button(
                                onClick = {
                                    MercadoPagoCheckout.Builder(
                                        context = context,
                                        checkoutType = MPCheckoutType.CardTransaction(
                                            order = MPOrder(
                                                orderId = orderId.trim(),
                                                clientToken = clientToken.trim(),
                                                amount = ORDER_AMOUNT,
                                                payer = MPPayer(email = ""),
                                            ),
                                        ),
                                    ).setPaymentMethodConfiguration(listOf(MPPaymentMethodConfig.Card()))
                                        .build()
                                        .show { result ->
                                            when (result) {
                                                is MercadoPagoCheckoutResult.Success ->
                                                    state = CheckoutState.CardTransactionSuccess(result.paymentData.orderStatus)
                                                is MercadoPagoCheckoutResult.Error ->
                                                    Toast.makeText(context, "Erro ${result.error.errorCode}", Toast.LENGTH_LONG).show()
                                                is MercadoPagoCheckoutResult.UserCancelled ->
                                                    Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                },
                                enabled = orderId.isNotBlank() && clientToken.isNotBlank(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(14.dp),
                            ) {
                                Text("Pagar agora", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                HorizontalDivider()

                // Save card section
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                    Text(
                        text = "Cadastrar cartão",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                        letterSpacing = 0.5.sp,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    when (val current = state) {
                        is CheckoutState.CardSaveSuccess -> {
                            ResultCard(
                                label = "Token gerado",
                                value = current.token,
                                hint = "token de tokenização do cartão",
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { state = CheckoutState.Idle }) {
                                Text("Novo cadastro")
                            }
                        }
                        else -> {
                            OutlinedButton(
                                onClick = {
                                    MercadoPagoCheckout.Builder(
                                        context = context,
                                        checkoutType = MPCheckoutType.CardSave,
                                    ).build().show { result ->
                                        when (result) {
                                            is MercadoPagoCheckoutResult.Success ->
                                                state = CheckoutState.CardSaveSuccess(result.paymentData.token)
                                            is MercadoPagoCheckoutResult.Error ->
                                                Toast.makeText(context, "Erro ${result.error.errorCode}", Toast.LENGTH_LONG).show()
                                            is MercadoPagoCheckoutResult.UserCancelled ->
                                                Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(14.dp),
                            ) {
                                Text("Salvar cartão", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ResultCard(
    label: String,
    value: String,
    hint: String,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp),
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF00A650),
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            tonalElevation = 2.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = value,
                    modifier = Modifier.weight(1f),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                    maxLines = 3,
                    fontWeight = FontWeight.Medium,
                )
                IconButton(
                    onClick = { clipboardManager.setText(AnnotatedString(value)) },
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copiar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
        Text(
            text = hint,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckoutExampleScreenPreview() {
    MercadoPagoSampleTheme {
        CheckoutExampleScreen()
    }
}
