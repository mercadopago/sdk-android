package com.mercadopago.sdk.android.foundation.typography

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseTypography
import com.mercadopago.android.sdk.foundation.R
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val TYPOGRAPHY_GROUP = "Typography"
private const val TYPOGRAPHY_TITLE_GROUP = "Title"
private const val TYPOGRAPHY_BODY_GROUP = "Body"

/**
 * Defines the set of text styles for Mercado Pago.
 * This structure is based on the provided design specifications.
 *
 * @property title Title text styles.
 * @property body Body text styles.
 */
data class MercadoPagoTypography(
    val title: MercadoPagoTitleTypography,
    val body: MercadoPagoBodyTypography,
)

/**
 * Defines the set of text styles for Mercado Pago.
 * This structure is based on the provided design specifications.
 *
 * @property smallSemibold Style for Title S Semibold.
 */
data class MercadoPagoTitleTypography(
    val smallSemibold: TextStyle,
)

/**
 * Defines the set of text styles for Mercado Pago.
 * This structure is based on the provided design specifications.
 *
 * @property mediumSemibold Style for Body M Semibold.
 * @property mediumRegular Style for Body M Regular.
 * @property smallSemibold Style for Body S Semibold.
 * @property smallRegular Style for Body S Regular.
 * @property extraSmallSemibold Style for Body XS Semibold.
 */
data class MercadoPagoBodyTypography(
    val mediumSemibold: TextStyle,
    val mediumRegular: TextStyle,
    val smallSemibold: TextStyle,
    val smallRegular: TextStyle,
    val extraSmallSemibold: TextStyle,
)

internal val ProximaNovaFontFamily = FontFamily(
    Font(R.font.proxima_nova_thin, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.proxima_nova_thin_italic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.proxima_nova_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.proxima_nova_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.proxima_nova_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.proxima_nova_regular_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.proxima_nova_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.proxima_nova_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.proxima_nova_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.proxima_nova_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.proxima_nova_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.proxima_nova_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.proxima_nova_extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.proxima_nova_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.proxima_nova_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.proxima_nova_black_italic, FontWeight.Black, FontStyle.Italic)
)

@ShowkaseTypography(name = "Title Small Semibold", group = TYPOGRAPHY_TITLE_GROUP)
internal val ProximaNovaTitleSmallSemibold = TextStyle(
    fontFamily = ProximaNovaFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp
)

@ShowkaseTypography(name = "Body Small Regular", group = TYPOGRAPHY_BODY_GROUP)
internal val ProximaNovaBodySmallRegular = TextStyle(
    fontFamily = ProximaNovaFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp
)

@ShowkaseTypography(name = "Body Small Semibold", group = TYPOGRAPHY_BODY_GROUP)
internal val ProximaNovaBodySmallSemibold = TextStyle(
    fontFamily = ProximaNovaFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp
)

@ShowkaseTypography(name = "Body Extra Small Semibold", group = TYPOGRAPHY_BODY_GROUP)
internal val ProximaNovaBodyExtraSmallSemibold = TextStyle(
    fontFamily = ProximaNovaFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.sp
)

@ShowkaseTypography(name = "Body Medium Regular", group = TYPOGRAPHY_BODY_GROUP)
internal val ProximaNovaBodyMediumRegular = TextStyle(
    fontFamily = ProximaNovaFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp
)

@ShowkaseTypography(name = "Body Medium Semibold", group = TYPOGRAPHY_BODY_GROUP)
internal val ProximaNovaMediumSemibold = TextStyle(
    fontFamily = ProximaNovaFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp
)

internal val MercadoPagoProximaNovaTypography = MercadoPagoTypography(
    title = MercadoPagoTitleTypography(
        smallSemibold = ProximaNovaTitleSmallSemibold,
    ),
    body = MercadoPagoBodyTypography(
        smallRegular = ProximaNovaBodySmallRegular,
        smallSemibold = ProximaNovaBodySmallSemibold,
        extraSmallSemibold = ProximaNovaBodyExtraSmallSemibold,
        mediumRegular = ProximaNovaBodyMediumRegular,
        mediumSemibold = ProximaNovaMediumSemibold,
    ),
)

@Preview(name = "Proxima Nova Title Typography", group = TYPOGRAPHY_GROUP)
@Composable
internal fun TitleTypographyPreview() {
    MercadoPagoTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            Text(
                text = "Title Small Semibold",
                style = MercadoPagoProximaNovaTypography.title.smallSemibold,
            )
        }
    }
}

@Preview(name = "Proxima Nova Body Typography", group = TYPOGRAPHY_GROUP)
@Composable
internal fun BodyTypographyPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            Text(
                text = "Body Small Regular",
                style = MercadoPagoProximaNovaTypography.body.smallRegular,
            )
            Text(
                text = "Body Small Semibold",
                style = MercadoPagoProximaNovaTypography.body.smallSemibold,
            )
            Text(
                text = "Body Extra Small Semibold",
                style = MercadoPagoProximaNovaTypography.body.extraSmallSemibold,
            )
            Text(
                text = "Body Medium Regular",
                style = MercadoPagoProximaNovaTypography.body.mediumRegular,
            )
            Text(
                text = "Body Medium Semibold",
                style = MercadoPagoProximaNovaTypography.body.mediumSemibold,
            )
        }
    }
}
