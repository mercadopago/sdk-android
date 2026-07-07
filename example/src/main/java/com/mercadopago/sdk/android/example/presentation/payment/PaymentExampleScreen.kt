package com.mercadopago.sdk.android.example.presentation.payment

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme
import kotlinx.coroutines.launch
import java.math.BigDecimal

private sealed interface PaymentExampleState {
    data object Idle : PaymentExampleState
    data class Success(val paymentData: MPPaymentData.Payment) : PaymentExampleState
}

@Composable
internal fun PaymentExampleScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var state by remember { mutableStateOf<PaymentExampleState>(PaymentExampleState.Idle) }

    val checkout = remember {
        MercadoPagoCheckout.Builder(
            context = context,
            checkoutType = MPCheckoutType.Payment(
                order = MPOrder(
                    orderId = "MOCK_ORD_APPROVED",
                    clientToken = "f30f8688-a2bf-4558-ba79-ddf108bece11",
                    amount = BigDecimal(100),
                    payer = MPPayer(email = "test@example.com"),
                )
            ),
        ).setPaymentMethodConfiguration(listOf(MPPaymentMethodConfig.Card()))
            .build()
    }

    when (val current = state) {
        PaymentExampleState.Idle -> Scaffold(
            modifier = modifier.fillMaxSize(),
        ) { padding ->
            PaymentIdleContent(
                modifier = Modifier.padding(padding),
                onStartPayment = {
                    checkout.show { result ->
                        when (result) {
                            is MercadoPagoCheckoutResult.Success ->
                                state = PaymentExampleState.Success(result.paymentData)

                            is MercadoPagoCheckoutResult.Error ->
                                Toast.makeText(
                                    context,
                                    "Ocorreu um erro ao processar o pagamento",
                                    Toast.LENGTH_LONG,
                                ).show()

                            is MercadoPagoCheckoutResult.UserCancelled ->
                                Toast.makeText(
                                    context,
                                    "Pagamento cancelado pelo usuário",
                                    Toast.LENGTH_LONG,
                                ).show()
                        }
                    }
                },
            )
        }

        is PaymentExampleState.Success -> PaymentSuccessContent(
            paymentData = current.paymentData,
            modifier = modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun PaymentIdleContent(
    onStartPayment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Payment",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Inicie um pagamento completo usando MPCheckoutType.Payment",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onStartPayment,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
        ) {
            Text(
                text = "Iniciar pagamento",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun PaymentSuccessContent(
    paymentData: MPPaymentData.Payment,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF00A650),
                modifier = Modifier.size(72.dp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Pagamento concluído",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(20.dp))

            PaymentDataRow(label = "Order ID", value = paymentData.orderId)
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            PaymentDataRow(label = "Status", value = paymentData.orderStatus)
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            PaymentDataRow(label = "Método", value = paymentData.paymentMethodId)
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            paymentData.installment?.let {
                PaymentDataRow(label = "Parcelas", value = it.toString())
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            }
            paymentData.transactionAmount?.let {
                PaymentDataRow(label = "Valor", value = "R$ $it")
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 2.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = paymentData.orderStatus,
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                        maxLines = 3,
                        fontWeight = FontWeight.Medium,
                    )
                    IconButton(
                        onClick = {
                            val clip = ClipData.newPlainText("orderStatus", paymentData.orderStatus).also { clipData ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    clipData.description.extras = PersistableBundle().apply {
                                        putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                                    }
                                }
                            }
                            (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                                .setPrimaryClip(clip)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Status copiado!")
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar status",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "status do pedido retornado pelo checkout",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun PaymentDataRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentIdlePreview() {
    MercadoPagoSampleTheme {
        PaymentIdleContent(onStartPayment = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentSuccessPreview() {
    MercadoPagoSampleTheme {
        PaymentSuccessContent(
            paymentData = MPPaymentData.Payment(
                orderId = "ORD-123",
                orderStatus = "approved",
                transactionAmount = BigDecimal("100.00"),
                paymentMethodId = "visa",
                paymentTypeId = "credit_card",
                payer = null,
                installment = 1,
                issuerId = null,
            ),
        )
    }
}
