package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
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

/**
 * A composable that displays a badge icon with a circular background.
 * The icon and color are determined by the [badgeType] parameter.
 *
 * @param modifier The modifier to be applied to the icon.
 * @param badgeType The type of badge that determines the icon and color to display.
 */
@Composable
fun MPBadgeIcon(
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
        icon,
        "",
        modifier
            .background(
                color = color,
                shape = CircleShape,
            ),
        tint = MercadoPagoAndesTheme.color.text.inverse,
    )
}

@Preview
@Composable
private fun MPBadgeIconPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPBadgeIcon(badgeType = BadgeType.Positive)
            MPBadgeIcon(badgeType = BadgeType.Negative)
            MPBadgeIcon(badgeType = BadgeType.Caution)
            MPBadgeIcon(badgeType = BadgeType.Informative)
        }
    }
}

/**
 * Enum representing the different types of badge icons available.
 * Each type corresponds to a specific feedback category with its own icon and color scheme.
 */
enum class BadgeType {
    /** Represents a positive feedback badge with a success icon. */
    Positive,

    /** Represents a negative feedback badge with an error icon. */
    Negative,

    /** Represents a caution feedback badge with a warning icon. */
    Caution,

    /** Represents an informative feedback badge with an info icon. */
    Informative,
}
