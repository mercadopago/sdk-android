package com.mercadopago.sdk.android.example.presentation.coremethods

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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.example.R
import com.mercadopago.sdk.android.example.data.model.Installment
import com.mercadopago.sdk.android.example.presentation.components.AlertDialog
import com.mercadopago.sdk.android.example.presentation.coremethods.state.PaymentScreenDialogState
import com.mercadopago.sdk.android.example.presentation.coremethods.state.PaymentScreenViewState
import com.mercadopago.sdk.android.example.presentation.components.CardTokenDialog
import com.mercadopago.sdk.android.example.presentation.components.IdentificationTypeSelectorField
import com.mercadopago.sdk.android.example.presentation.components.InstallmentListDropDownField
import com.mercadopago.sdk.android.example.presentation.components.Label
import com.mercadopago.sdk.android.example.presentation.logs.DebugLogsScreen
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme
import com.mercadopago.sdk.android.example.ui.components.IdentificationName
import com.mercadopago.sdk.android.example.ui.components.fields.CardNumberTextFieldExample
import com.mercadopago.sdk.android.example.ui.components.fields.ExpirationDateTextFieldExample
import com.mercadopago.sdk.android.example.ui.components.fields.SecurityCodeTextFieldExample
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PaymentExampleScreen(
    viewModel: PaymentScreenViewModel = koinViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.getIdentificationTypes()
    }

    PaymentExampleScreenContent(
        viewState = viewState,
        cardNumberState = viewModel.cardNumberPCIState,
        expirationDateState = viewModel.expirationDatePCIState,
        securityCodeState = viewModel.securityCodePCIState,
        onGenerateCardToken = { viewModel.generateToken() },
        onExpirationDateEvent = viewModel::onExpirationDateEvent,
        onSecurityCodeEvent = viewModel::onSecurityCodeEvent,
        onCardNumberEvent = viewModel::onCardNumberEvent,
        onSelectIdentification = viewModel::onIdentificationTypeChanged,
        onIdentificationTypeChanged = viewModel::onIdentificationTypeValueChanged,
        onSelectedInstallment = viewModel::onInstallmentSelected,
        onCardHolderNameChanged = viewModel::onCardHolderNameChanged,
        onDialogStateChange = viewModel::onDialogStateChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PaymentExampleScreenContent(
    modifier: Modifier = Modifier,
    viewState: PaymentScreenViewState,
    cardNumberState: PCIFieldState,
    expirationDateState: PCIFieldState,
    securityCodeState: PCIFieldState,
    onGenerateCardToken: () -> Unit,
    onExpirationDateEvent: (ExpirationDateTextFieldEvent) -> Unit,
    onSecurityCodeEvent: (SecurityCodeTextFieldEvent) -> Unit,
    onCardNumberEvent: (CardNumberTextFieldEvent) -> Unit,
    onSelectIdentification: (IdentificationType) -> Unit,
    onIdentificationTypeChanged: (String) -> Unit,
    onSelectedInstallment: (Installment) -> Unit,
    onCardHolderNameChanged: (String) -> Unit,
    onDialogStateChange: (PaymentScreenDialogState) -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current
    when (viewState.dialogState) {
        is PaymentScreenDialogState.CardToken -> CardTokenDialog(
            token = viewState.dialogState.token,
            onDismiss = {
                onDialogStateChange(PaymentScreenDialogState.Hidden)
                clipboardManager.setText(AnnotatedString(viewState.dialogState.token))
            },
        )

        is PaymentScreenDialogState.Error -> AlertDialog(
            title = viewState.dialogState.title,
            description = viewState.dialogState.description,
            onDismissRequest = {
                onDialogStateChange(PaymentScreenDialogState.Hidden)
            },
        )

        PaymentScreenDialogState.Hidden -> Unit
    }

    BottomSheetScaffold(
        sheetContent = {
            DebugLogsScreen()
        }
    ) {
        Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column {
                    Spacer(Modifier.height(16.dp))
                    CardNumberTextFieldExample(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        state = cardNumberState,
                        cardNumberState = viewState.cardNumberState,
                        onCardNumberEvent = onCardNumberEvent
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                        ExpirationDateTextFieldExample(
                            Modifier.weight(1f),
                            state = expirationDateState,
                            expirationDateState = viewState.expirationDateState,
                            onExpirationDateEvent = onExpirationDateEvent
                        )
                        Spacer(Modifier.width(16.dp))
                        SecurityCodeTextFieldExample(
                            Modifier.weight(1f),
                            state = securityCodeState,
                            securityCodeState = viewState.secureCodeState,
                            onSecurityCodeEvent = onSecurityCodeEvent
                        )
                    }
                    IdentificationName(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        identificationState = viewState.identificationState,
                        onCardHolderNameChanged = onCardHolderNameChanged
                    )
                    Spacer(Modifier.size(16.dp))
                    IdentificationTypeSelectorField(
                        state = viewState.identificationState,
                        onSelectIdentification = onSelectIdentification,
                        onIdentificationTypeChanged = onIdentificationTypeChanged
                    )
                    Spacer(Modifier.size(16.dp))
                    InstallmentListDropDownField(
                        state = viewState.installmentsState,
                        onSelectedInstallment = onSelectedInstallment
                    )
                    Spacer(Modifier.size(16.dp))

                    Button(
                        shape = MaterialTheme.shapes.small,
                        onClick = onGenerateCardToken,
                        modifier = Modifier
                            .align(Alignment.End)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_padlock_closed),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                contentDescription = null,
                            )
                            Spacer(Modifier.width(4.dp))
                            Label(
                                text = "Pay",
                                textColor = MaterialTheme.colorScheme.onPrimary,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Payment Screen Example", showBackground = true)
@Composable
internal fun PaymentExampleScreenPreview() {
    MercadoPagoSampleTheme {
        PaymentExampleScreen()
    }
}
