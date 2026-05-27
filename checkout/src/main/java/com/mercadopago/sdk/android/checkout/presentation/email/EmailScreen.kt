package com.mercadopago.sdk.android.checkout.presentation.email

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.checkout.presentation.state.EmailScreenState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.EmailViewModel
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.inputs.MPSimpleTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextFieldEvent
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

/**
 * @param viewModel Detém o estado e a lógica de validação do email.
 * @param labels Textos exibidos na tela (título, label, placeholder, botão e mensagens de erro).
 * @param baseEmail Usado apenas para habilitar o botão no início quando o caller já conhece
 *   um email válido. Não pré-preenche o campo visualmente (PCIFieldState não expõe seed público).
 * @param deeplink Acionado ao clicar no botão. Será substituído por callback no futuro
 *   (depende do PR de installment).
 * @param onBackClick Callback do botão de voltar do header.
 * @param onContinueClick Callback do botão de continuar; recebe o [deeplink] para navegação.
 */
@Composable
internal fun EmailScreen(
    viewModel: EmailViewModel,
    labels: EmailScreenState.Labels,
    baseEmail: String? = null,
    deeplink: String = "",
    onBackClick: () -> Unit = {},
    onContinueClick: (deeplink: String) -> Unit = {},
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(labels, baseEmail) {
        viewModel.initialize(labels, baseEmail)
    }

    viewState?.let { state ->
        EmailScreenContent(
            state = state,
            errorMessage = viewModel.resolveErrorMessage(state),
            onBackClick = onBackClick,
            onEmailChange = viewModel::onEmailChanged,
            onContinueClick = { onContinueClick(deeplink) },
        )
    }
}

@Composable
private fun EmailScreenContent(
    state: EmailScreenState,
    errorMessage: String,
    onBackClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onContinueClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MercadoPagoTheme.color.background.primary),
    ) {
        MPHeader(
            title = state.labels.title,
            onBackClick = onBackClick,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MercadoPagoTheme.spacing.paddings.xtiny,
                        end = MercadoPagoTheme.spacing.paddings.xtiny,
                        top = MercadoPagoTheme.spacing.paddings.xsmall,
                    ),
            ) {
                EmailField(
                    state = state,
                    errorMessage = errorMessage,
                    onValueChange = onEmailChange,
                )
            }
        }

        MPFixedFooter(
            title = "",
            modifier = Modifier.align(Alignment.BottomCenter),
            button = MPFixedFooterButtonData(
                text = state.labels.buttonLabel,
                enabled = state.isButtonEnabled,
                onClick = onContinueClick,
            ),
        )
    }
}

@Composable
private fun EmailField(
    state: EmailScreenState,
    errorMessage: String,
    onValueChange: (String) -> Unit,
) {
    val fieldState = rememberPCIFieldState()
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    MPSimpleTextField(
        state = fieldState,
        isFocused = isFocused,
        showPlaceHolder = true,
        label = state.labels.fieldLabel,
        placeHolder = state.labels.fieldPlaceholder,
        error = errorMessage,
        onEvent = { event ->
            if (event is SimpleTextFieldEvent.OnValueChanged) {
                onValueChange(event.value)
            }
        },
    )
}

@Preview(showBackground = true, name = "Email Screen - Empty")
@Composable
private fun EmailScreenEmptyPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        EmailScreenContent(
            state = EmailScreenState(labels = previewTranslate()),
            errorMessage = "",
            onBackClick = {},
            onEmailChange = {},
            onContinueClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Email Screen - Filled")
@Composable
private fun EmailScreenFilledPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        EmailScreenContent(
            state = EmailScreenState(
                labels = previewTranslate(),
                email = "maria.sosa@gmail.com",
                isError = false,
                isButtonEnabled = true,
            ),
            errorMessage = "",
            onBackClick = {},
            onEmailChange = {},
            onContinueClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Email Screen - Error")
@Composable
private fun EmailScreenErrorPreview() {
    val translate = previewTranslate()
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        EmailScreenContent(
            state = EmailScreenState(
                labels = translate,
                email = "maria.sosa@",
                isError = true,
                isButtonEnabled = false,
            ),
            errorMessage = translate.errorEmailInvalid,
            onBackClick = {},
            onEmailChange = {},
            onContinueClick = {},
        )
    }
}

private fun previewTranslate() =
    EmailScreenState.Labels(
        title = "Completá el e-mail",
        fieldLabel = "E-mail",
        fieldPlaceholder = "maria.sosa@gmail.com",
        buttonLabel = "Continuar",
        errorFieldEmpty = "Ingresá un e-mail",
        errorEmailInvalid = "El e-mail no es válido",
        errorFieldRequired = "Este campo es obligatorio",
    )
