package com.mercadopago.sdk.android.checkout.presentation.cvv

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.checkout.domain.model.CVVFieldConfig
import com.mercadopago.sdk.android.checkout.domain.model.CVVScreenData
import com.mercadopago.sdk.android.checkout.presentation.state.CVVScreenState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CVVViewModel
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.inputs.MPSecurityCodeTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val CVV_SCREEN_GROUP = "CVV_SCREEN"

@Composable
internal fun CVVScreen(
    viewModel: CVVViewModel,
    onBackPressed: () -> Unit = {},
) {
    val viewState by viewModel.viewState.collectAsState()
    val cvvPCIState = rememberPCIFieldState()

    CVVScreenContent(
        viewState = viewState,
        cvvPCIState = cvvPCIState,
        onBackPressed = onBackPressed,
        onCVVEvent = { event ->
            when (event) {
                is SecurityCodeTextFieldEvent.OnLengthChanged ->
                    viewModel.onCVVLengthChanged(event.length)
                else -> Unit
            }
        },
        onContinue = viewModel::onContinue,
    )
}

@Composable
internal fun CVVScreenContent(
    viewState: CVVScreenState,
    cvvPCIState: PCIFieldState,
    onBackPressed: () -> Unit = {},
    onCVVEvent: (SecurityCodeTextFieldEvent) -> Unit = {},
    onContinue: () -> Unit = {},
) {
    val screenData = viewState.screenData ?: return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                MPHeader(
                    modifier = Modifier.fillMaxWidth(),
                    title = screenData.headerTitle,
                    onBackClick = onBackPressed,
                ) {
                    Spacer(modifier = Modifier.size(MercadoPagoTheme.spacing.paddings.xsmall))
                    MPSecurityCodeTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MercadoPagoTheme.spacing.paddings.xtiny),
                        state = cvvPCIState,
                        securityCodeSize = screenData.expectedLength,
                        showPlaceHolder = true,
                        label = screenData.field.label,
                        helper = screenData.field.helper,
                        placeHolder = screenData.field.placeholder,
                        error = viewState.errorMessage.orEmpty(),
                        onClickTooltip = {},
                        onEvent = onCVVEvent,
                    )
                }
            }
            MPFixedFooter(
                title = "",
                button = MPFixedFooterButtonData(
                    text = screenData.continueButtonLabel,
                    enabled = viewState.isContinueEnabled,
                    onClick = onContinue,
                ),
            )
        }
    }
}

@Preview(name = "CVV Screen", group = CVV_SCREEN_GROUP, showBackground = true)
@Composable
private fun CVVScreenPreview() {
    MercadoPagoTheme {
        CVVScreenContent(
            viewState = CVVScreenState(
                screenData = CVVScreenData(
                    headerTitle = "Ingresá el código de seguridad",
                    field = CVVFieldConfig(
                        label = "Código de seguridad",
                        placeholder = "Ej.: 123",
                        helper = "Está en el reverso de tu tarjeta.",
                    ),
                    continueButtonLabel = "Continuar",
                    expectedLength = 3,
                ),
                isContinueEnabled = false,
            ),
            cvvPCIState = rememberPCIFieldState(),
        )
    }
}

@Preview(name = "CVV Screen Amex Continue Enabled", group = CVV_SCREEN_GROUP, showBackground = true)
@Composable
private fun CVVScreenAmexPreview() {
    MercadoPagoTheme {
        CVVScreenContent(
            viewState = CVVScreenState(
                screenData = CVVScreenData(
                    headerTitle = "Ingresá el código de seguridad",
                    field = CVVFieldConfig(
                        label = "Código de seguridad",
                        placeholder = "Ej.: 1234",
                        helper = "Está en el frente de tu tarjeta.",
                    ),
                    continueButtonLabel = "Continuar",
                    expectedLength = 4,
                ),
                isContinueEnabled = true,
            ),
            cvvPCIState = rememberPCIFieldState(),
        )
    }
}
