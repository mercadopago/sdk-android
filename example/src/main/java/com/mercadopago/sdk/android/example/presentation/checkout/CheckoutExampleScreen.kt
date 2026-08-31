package com.mercadopago.sdk.android.example.presentation.checkout

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mercadopago.sdk.android.checkout.core.MercadoPagoCheckout
import com.mercadopago.sdk.android.checkout.core.extensions.withReviewAndConfirm
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme

private sealed interface CheckoutState {
    data object Idle : CheckoutState
    data class CardSaveSuccess(val token: String) : CheckoutState
    data class CardTransactionSuccess(val orderStatus: String) : CheckoutState
    data class PaymentSuccess(val orderStatus: String) : CheckoutState
}

@Composable
internal fun CheckoutExampleScreen(
    flowType: String = "card_save",
    modifier: Modifier = Modifier,
) {
    val showOrderFields = flowType != "card_save"
    val context = LocalContext.current
    var state by remember { mutableStateOf<CheckoutState>(CheckoutState.Idle) }
    var orderId by remember { mutableStateOf("") }
    var clientToken by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("15.00") }
    var isEditingPrice by remember { mutableStateOf(false) }

    ExampleScreenWrapper(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 30.dp),
        ) {
            // ── Store header ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFF009EE3), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp),
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Demo Store",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Simulação de checkout",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                }
            }

            HorizontalDivider()

            // ── Product card ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 2.dp,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color(0xFF009EE3), Color(0xFF006FA8)),
                                    ),
                                    RoundedCornerShape(12.dp),
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(36.dp),
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Produto de Demonstração",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Código: DEMO-001",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF009EE3).copy(alpha = 0.1f),
                            ) {
                                Text(
                                    text = "Disponível",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF009EE3),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(12.dp))

                    // Price — display or edit
                    if (isEditingPrice) {
                        val focusRequester = remember { FocusRequester() }
                        LaunchedEffect(Unit) { focusRequester.requestFocus() }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            OutlinedTextField(
                                value = priceInput,
                                onValueChange = { priceInput = it.filter { c -> c.isDigit() || c == '.' } },
                                label = { Text("Valor (R$)") },
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(focusRequester)
                                    .testTag("checkout.priceInput"),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done,
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { isEditingPrice = false },
                                ),
                                shape = RoundedCornerShape(10.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { isEditingPrice = false },
                                modifier = Modifier.testTag("checkout.priceConfirm"),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Confirmar",
                                    tint = Color(0xFF00A650),
                                    modifier = Modifier.size(32.dp),
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkout.priceDisplay")
                                .clickable { isEditingPrice = true },
                        ) {
                            Text(
                                text = "R$ $priceInput",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(11.dp),
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "toque para editar",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                                )
                            }
                        }
                    }
                }
            }

            // ── Order config section ──
            if (showOrderFields) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "CONFIGURAÇÃO DO PEDIDO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        letterSpacing = 1.sp,
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
                            modifier = Modifier.weight(1f).testTag("checkout.orderId"),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.labelSmall,
                            shape = RoundedCornerShape(10.dp),
                        )
                        OutlinedTextField(
                            value = clientToken,
                            onValueChange = { clientToken = it },
                            label = { Text("Client Token", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f).testTag("checkout.clientToken"),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.labelSmall,
                            shape = RoundedCornerShape(10.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Order summary ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 1.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Total", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "R$ $priceInput",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Action buttons ──
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                val order = MPOrder(orderId = orderId.trim(), clientToken = clientToken.trim())
                val onPayClick: (() -> Unit)? = when (flowType) {
                    "card_transaction" -> ({
                        MercadoPagoCheckout.Builder(
                            context,
                            MPCheckoutType.CardTransaction(order, sellerInfo = MPSellerInfo(name = "Adidas")),
                        )
                            .setPaymentMethodConfiguration(listOf(MPPaymentMethodConfig.Card()))
                            .withReviewAndConfirm()
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
                    })
                    "payment" -> ({
                        MercadoPagoCheckout.Builder(context, MPCheckoutType.Payment(order))
                            .setPaymentMethodConfiguration(listOf(MPPaymentMethodConfig.Card()))
                            .build()
                            .show { result ->
                                when (result) {
                                    is MercadoPagoCheckoutResult.Success ->
                                        state = CheckoutState.PaymentSuccess(result.paymentData.orderStatus)
                                    is MercadoPagoCheckoutResult.Error ->
                                        Toast.makeText(context, "Erro ${result.error.errorCode}", Toast.LENGTH_LONG).show()
                                    is MercadoPagoCheckoutResult.UserCancelled ->
                                        Toast.makeText(context, "Cancelado", Toast.LENGTH_SHORT).show()
                                }
                            }
                    })
                    else -> null
                }
                if (onPayClick != null) {
                    Button(
                        onClick = onPayClick,
                        enabled = orderId.isNotBlank() && clientToken.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("checkout.payNow"),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Text("Pagar agora", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                } else {
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
                            .height(52.dp)
                            .testTag("checkout.saveCard"),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Text("Salvar cartão", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        when (val current = state) {
            is CheckoutState.CardTransactionSuccess -> PaymentSuccessScreen(
                orderStatus = current.orderStatus,
                onNewPayment = { state = CheckoutState.Idle },
                modifier = Modifier.fillMaxSize(),
            )
            is CheckoutState.PaymentSuccess -> PaymentSuccessScreen(
                orderStatus = current.orderStatus,
                onNewPayment = { state = CheckoutState.Idle },
                modifier = Modifier.fillMaxSize(),
            )
            is CheckoutState.CardSaveSuccess -> CardSaveSuccessScreen(
                token = current.token,
                onNewSave = { state = CheckoutState.Idle },
                modifier = Modifier.fillMaxSize(),
            )
            CheckoutState.Idle -> Unit
        }
    }
}

@Composable
private fun PaymentSuccessScreen(
    orderStatus: String,
    onNewPayment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    Column(
        modifier = modifier
            .background(Color.White)
            .testTag("checkout.paymentSuccess"),
    ) {
        // Hero section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF00A650), Color(0xFF007A3D)),
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(Color.White.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(72.dp),
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Pagamento concluído!",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sua compra foi processada com sucesso",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
            )
        }

        // Detail section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "STATUS DO PEDIDO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF00A650),
                    letterSpacing = 1.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 2.dp,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = orderStatus,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("checkout.paymentSuccess.status"),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        IconButton(onClick = { clipboardManager.setText(AnnotatedString(orderStatus)) }) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copiar",
                                tint = Color(0xFF00A650),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                }
            }

            Button(
                onClick = onNewPayment,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("checkout.paymentSuccess.newPayment"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A650)),
            ) {
                Text("Novo pagamento", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun CardSaveSuccessScreen(
    token: String,
    onNewSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    Column(
        modifier = modifier
            .background(Color.White)
            .testTag("checkout.cardSaveSuccess"),
    ) {
        // Hero section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF009EE3), Color(0xFF006FA8)),
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(Color.White.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp),
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Cartão salvo!",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Seu cartão foi tokenizado com sucesso",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
            )
        }

        // Detail section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "TOKEN DO CARTÃO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF009EE3),
                    letterSpacing = 1.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 2.dp,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = token,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("checkout.cardSaveSuccess.token"),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                        )
                        IconButton(onClick = { clipboardManager.setText(AnnotatedString(token)) }) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copiar",
                                tint = Color(0xFF009EE3),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                }
            }

            Button(
                onClick = onNewSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("checkout.cardSaveSuccess.newSave"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009EE3)),
            ) {
                Text("Novo cadastro", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun ExampleScreenWrapper(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.border(width = 3.dp, color = Color.Red)) {
        content()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(vertical = 6.dp)
                .align(Alignment.TopCenter),
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
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckoutExampleScreenPreview() {
    MercadoPagoSampleTheme {
        CheckoutExampleScreen()
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun PaymentSuccessScreenPreview() {
    MercadoPagoSampleTheme {
        ExampleScreenWrapper(modifier = Modifier.fillMaxSize()) {
            PaymentSuccessScreen(
                orderStatus = "processed",
                onNewPayment = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun CardSaveSuccessScreenPreview() {
    MercadoPagoSampleTheme {
        ExampleScreenWrapper(modifier = Modifier.fillMaxSize()) {
            CardSaveSuccessScreen(
                token = "a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4",
                onNewSave = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
