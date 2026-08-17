package com.mercadopago.sdk.android.checkout.presentation.methodselection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.MethodSelectionScreenState
import com.mercadopago.sdk.android.components.MPButton
import com.mercadopago.sdk.android.components.MPHeader
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun MethodSelectionScreen(
    screenState: MethodSelectionScreenState,
    onOptionTap: (MethodSelectionOption) -> Unit,
    onCtaTap: () -> Unit,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        MPHeader(
            modifier = Modifier.weight(1f),
            title = screenState.headerTitle,
            onBackClick = onBack,
        ) {
            Column {
                screenState.options.forEach { option ->
                    MethodSelectionOptionRow(
                        option = option,
                        isArrowLayout = screenState.isArrowLayout,
                        isSelected = option.id == screenState.selectedOptionId,
                        onTap = { tappedOption -> onOptionTap(tappedOption) },
                    )
                }
            }
        }
        if (screenState.footerState.isVisible) {
            MethodSelectionFooter(
                footerState = screenState.footerState,
                onCtaTap = onCtaTap,
            )
        }
    }
}

@Composable
private fun MethodSelectionFooter(
    footerState: FooterState,
    onCtaTap: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MercadoPagoTheme.color.background.primary)
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                vertical = MercadoPagoTheme.spacing.paddings.xtiny,
            ),
    ) {
        if (footerState.title.isNotEmpty() || footerState.subtitle != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MPText(
                    text = footerState.title,
                    style = MercadoPagoTheme.typography.body.emphasis.large,
                    color = MercadoPagoTheme.color.text.primary,
                )
                footerState.subtitle?.let { amount ->
                    MPText(
                        text = amount,
                        style = MercadoPagoTheme.typography.heading.default.medium,
                        color = MercadoPagoTheme.color.text.primary,
                    )
                }
            }
            Spacer(modifier = Modifier.height(MercadoPagoTheme.spacing.paddings.micro))
        }
        footerState.buttonLabel?.let { label ->
            MPButton(
                text = label,
                modifier = Modifier.fillMaxWidth(),
                enabled = footerState.buttonState?.enabled ?: false,
                isLoading = footerState.buttonState?.isLoading ?: false,
                onClick = onCtaTap,
            )
        }
    }
}
