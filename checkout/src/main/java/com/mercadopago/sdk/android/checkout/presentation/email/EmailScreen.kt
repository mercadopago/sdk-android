package com.mercadopago.sdk.android.checkout.presentation.email

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.checkout.presentation.state.EmailFieldState
import com.mercadopago.sdk.android.checkout.presentation.state.EmailScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.EmailViewModel
import com.mercadopago.sdk.android.components.MPFixedFooter
import com.mercadopago.sdk.android.components.MPFixedFooterButtonData
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.inputs.MPSimpleTextField
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

@Composable
internal fun EmailScreen(
    viewModel: EmailViewModel,
    onBackClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    val viewState by viewModel.viewState.collectAsState()

    viewState?.let { state ->
        EmailScreenContent(
            state = state,
            onBackClick = onBackClick,
            onEmailChange = viewModel::onEmailChanged,
            onFocusChange = viewModel::onFocusChanged,
            onContinueClick = { onClick() },
        )
    }
}

@Composable
private fun EmailScreenContent(
    state: EmailScreenState,
    onBackClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onContinueClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MercadoPagoTheme.color.background.primary),
    ) {
        MPHeader(
            title = state.title,
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
                    fieldState = state.fieldState,
                    onValueChange = onEmailChange,
                    onFocusChange = onFocusChange,
                )
            }
        }

        MPFixedFooter(
            title = "",
            modifier = Modifier.align(Alignment.BottomCenter),
            button = MPFixedFooterButtonData(
                text = state.buttonLabel,
                enabled = state.isButtonEnabled,
                onClick = onContinueClick,
            ),
        )
    }
}

@Composable
private fun EmailField(
    fieldState: EmailFieldState,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
) {
    MPSimpleTextField(
        value = fieldState.value,
        isFocused = fieldState.isFocused,
        showPlaceHolder = fieldState.showPlaceHolder,
        label = fieldState.label,
        placeHolder = fieldState.placeHolder,
        error = fieldState.error,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        onValueChange = onValueChange,
        onFocusChange = onFocusChange,
    )
}

@Preview(showBackground = true, name = "Email Screen - Empty")
@Composable
private fun EmailScreenEmptyPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        EmailScreenContent(
            state = previewState(),
            onBackClick = {},
            onEmailChange = {},
            onFocusChange = {},
            onContinueClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Email Screen - Filled")
@Composable
private fun EmailScreenFilledPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        EmailScreenContent(
            state = previewState().copy(
                isButtonEnabled = true,
                fieldState = previewState().fieldState.copy(
                    value = "maria.sosa@gmail.com",
                    isValid = true,
                ),
            ),
            onBackClick = {},
            onEmailChange = {},
            onFocusChange = {},
            onContinueClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Email Screen - Error")
@Composable
private fun EmailScreenErrorPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        EmailScreenContent(
            state = previewState().copy(
                fieldState = previewState().fieldState.copy(
                    value = "maria.sosa@",
                    error = "El e-mail no es válido",
                ),
            ),
            onBackClick = {},
            onEmailChange = {},
            onFocusChange = {},
            onContinueClick = {},
        )
    }
}

private fun previewState() =
    EmailScreenState(
        title = "Completá el e-mail",
        buttonLabel = "Continuar",
        fieldState = EmailFieldState(
            label = "E-mail",
            placeHolder = "maria.sosa@gmail.com",
            showPlaceHolder = true,
            validation = ValidationState(
                errorEmpty = "Ingresá un e-mail",
                errorInvalid = "El e-mail no es válido",
            ),
        ),
    )
