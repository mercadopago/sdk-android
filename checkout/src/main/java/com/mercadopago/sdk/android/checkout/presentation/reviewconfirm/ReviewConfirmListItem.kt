package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmItemUiModel
import com.mercadopago.sdk.android.components.MPButton
import com.mercadopago.sdk.android.components.MPButtonSize
import com.mercadopago.sdk.android.components.MPButtonStyle
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun ReviewConfirmListItem(
    item: ReviewConfirmItemUiModel,
    onChangeClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MercadoPagoTheme.spacing.paddings.micro),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.gap.xnano),
        ) {
            MPText(
                text = item.label,
                style = MercadoPagoTheme.typography.body.emphasis.medium,
                color = MercadoPagoTheme.color.text.primary,
            )
            item.value?.let { value ->
                MPText(
                    text = value,
                    style = MercadoPagoTheme.typography.body.default.large,
                    color = MercadoPagoTheme.color.text.secondary,
                )
            }
        }

        item.buttonLabel?.let { buttonLabel ->
            MPButton(
                text = buttonLabel,
                style = MPButtonStyle.Quiet,
                size = MPButtonSize.Medium,
                onClick = { onChangeClick(item.type) },
                modifier = Modifier.padding(start = MercadoPagoTheme.spacing.paddings.xmicro),
            )
        }
    }
}
