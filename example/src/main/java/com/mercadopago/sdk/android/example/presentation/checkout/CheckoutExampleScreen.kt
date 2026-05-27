package com.mercadopago.sdk.android.example.presentation.checkout

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
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
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethod
import android.widget.Toast
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme
import kotlinx.coroutines.launch

private sealed interface CheckoutState {
    data object Idle : CheckoutState
    data class Success(val token: String) : CheckoutState
}

@Composable
internal fun CheckoutExampleScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var state by remember { mutableStateOf<CheckoutState>(CheckoutState.Idle) }

    val checkout = remember {
        MercadoPagoCheckout.Builder(
            context = context,
            checkoutType = MPCheckoutType.CardSave,
        ).setPaymentMethods(listOf(MPPaymentMethod.Card()))
            .build()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .border(width = 3.dp, color = Color.Red),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "example screen",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
            )
        }

        when (val current = state) {
            CheckoutState.Idle -> Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            ) { padding ->
                CheckoutIdleContent(
                    modifier = Modifier.padding(padding),
                    onRegisterCard = {
                        checkout.show { result ->
                            when (result) {
                                is MercadoPagoCheckoutResult.Success ->
                                    state = CheckoutState.Success(result.paymentData.token)

                                is MercadoPagoCheckoutResult.Error ->
                                    Toast.makeText(
                                        context,
                                        "Erro ${result.error.errorCode}: ${result.error.errorMessage}",
                                        Toast.LENGTH_LONG,
                                    ).show()

                                is MercadoPagoCheckoutResult.UserCancelled -> {
                                    val fieldsInfo = when (val ctx = result.context) {
                                        is MPUserCancelledContext.CardForm ->
                                            ctx.context.fields.joinToString(", ") { field ->
                                                "${field.field.name}: ${field.state::class.simpleName}"
                                            }
                                    }
                                    val message = "CardForm (installmentsWasPresented=" +
                                        "${ctx.installmentsWasPresented})\n$fieldsInfo"
                                    Toast.makeText(
                                        context,
                                        "Cancelado pelo usuário\n$message",
                                        Toast.LENGTH_LONG,
                                    ).show()
                                }
                            }
                        }
                    },
                )
            }

            is CheckoutState.Success -> CheckoutSuccessContent(
                token = current.token,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            )
        }
    }
}

@Composable
private fun CheckoutIdleContent(
    onRegisterCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Forma de pagamento",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Escolha como deseja pagar",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "CARTÃO DE CRÉDITO OU DÉBITO",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
            letterSpacing = 0.8.sp,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            tonalElevation = 2.dp,
            shadowElevation = 1.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(44.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Novo cartão",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "Crédito ou débito",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onRegisterCard,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
        ) {
            Text(
                text = "Cadastrar cartão",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun CheckoutSuccessContent(
    token: String,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
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
                text = "Token recebido",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                        text = token,
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                        maxLines = 3,
                        fontWeight = FontWeight.Medium,
                    )
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(token))
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Token copiado!")
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copiar token",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "use este token para testar tokenização do cartão",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckoutIdlePreview() {
    MercadoPagoSampleTheme {
        CheckoutIdleContent(onRegisterCard = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckoutSuccessPreview() {
    MercadoPagoSampleTheme {
        CheckoutSuccessContent(token = "abc123def456ghi789jkl012mno345pqr678stu901")
    }
}
