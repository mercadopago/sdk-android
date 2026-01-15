package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

enum class BadgeType {
    Positive,
    Negative,
    Caution,
    Informative,
}

@Composable
fun BadgeIcons(
    modifier: Modifier = Modifier,
    badgeType: BadgeType,
) {
    val icon = when (badgeType) {
        BadgeType.Positive -> painterResource(R.drawable.ic_feedback_positive)
        BadgeType.Negative -> painterResource(R.drawable.ic_feedback_error)
        BadgeType.Caution -> painterResource(R.drawable.ic_feedback_caution)
        BadgeType.Informative -> painterResource(R.drawable.ic_feedback_info)
    }

    val color: Color = when (badgeType) {
        BadgeType.Positive -> MercadoPagoAndesTheme.color.feedback.positive.iconLoud
        BadgeType.Negative -> MercadoPagoAndesTheme.color.feedback.negative.iconLoud
        BadgeType.Caution -> MercadoPagoAndesTheme.color.feedback.caution.iconLoud
        BadgeType.Informative -> MercadoPagoAndesTheme.color.feedback.informative.iconLoud
    }

    Icon(
        icon, "",
        modifier
            .background(
                color = color,
                shape = CircleShape,
            )
            .padding(3.dp),
        tint = MercadoPagoAndesTheme.color.text.inverse
    )
}

@Preview
@Composable
fun BadgeIconsPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            BadgeIcons(badgeType = BadgeType.Positive)
            BadgeIcons(badgeType = BadgeType.Negative)
            BadgeIcons(badgeType = BadgeType.Caution)
            BadgeIcons(badgeType = BadgeType.Informative)
        }
    }
}
