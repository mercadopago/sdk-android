package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPIcon
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.model.MPIconColor
import com.mercadopago.sdk.android.components.model.MPIconSize
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun ReviewConfirmSellerSection(
    sellerName: String?,
    sellerIconUrl: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        MPIcon(
            url = sellerIconUrl,
            size = MPIconSize.XLarge,
            color = MPIconColor.Primary,
            applyTint = false,
            showBorder = true,
            isDecorative = true,
        )

        sellerName?.let {
            Spacer(modifier = Modifier.height(8.dp))

            MPText(
                text = it,
                style = MercadoPagoTheme.typography.body.emphasis.large,
                color = MercadoPagoTheme.color.text.primary,
            )
        }
    }
}
