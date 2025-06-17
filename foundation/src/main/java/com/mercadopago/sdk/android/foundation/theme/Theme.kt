package com.mercadopago.sdk.android.foundation.theme

import androidx.annotation.RestrictTo
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.color.BackgroundColor
import com.mercadopago.sdk.android.foundation.color.FeedbackColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoColor
import com.mercadopago.sdk.android.foundation.color.OutlineColor
import com.mercadopago.sdk.android.foundation.color.TextColor
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoOutline
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoShape
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoSpacing
import com.mercadopago.sdk.android.foundation.theme.default.MercadoPagoDefaultThemes
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoBodyTypography
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoTitleTypography
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoTypography

/**
 * @suppress
 * CompositionLocal that provides the current MercadoPago theme configuration.
 * This is used to propagate theme values down the composition tree.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal val LocalMercadoPagoTheme = compositionLocalOf {
    MercadoPagoThemeProvider(
        color = MercadoPagoColor(
            accent = Color.Unspecified,
            accentFirstVariant = Color.Unspecified,
            accentSecondVariant = Color.Unspecified,
            accentYellow = Color.Unspecified,
            accentPositive = Color.Unspecified,
            accentNegative = Color.Unspecified,
            background = BackgroundColor(
                primary = Color.Unspecified,
                secondary = Color.Unspecified,
                tertiary = Color.Unspecified,
                inverted = Color.Unspecified,
            ),
            text = TextColor(
                primary = Color.Unspecified,
                secondary = Color.Unspecified,
                accent = Color.Unspecified,
                disabled = Color.Unspecified,
                negative = Color.Unspecified,
                inverted = Color.Unspecified,
            ),
            secondary = Color.Unspecified,
            secondaryFirstVariant = Color.Unspecified,
            secondarySecondVariant = Color.Unspecified,
            outline = OutlineColor(
                primary = Color.Unspecified,
                secondary = Color.Unspecified,
            ),
            feedback = FeedbackColor(
                positive = Color.Unspecified,
                negative = Color.Unspecified,
                positiveSecondary = Color.Unspecified,
            ),
        ),
        spacing = MercadoPagoSpacing(
            xxs = 0.dp,
            xs = 0.dp,
            s = 0.dp,
            m = 0.dp,
            l = 0.dp,
            xl = 0.dp,
            xxl = 0.dp,
        ),
        shape = MercadoPagoShape(
            xxs = RoundedCornerShape(0.dp),
            xs = RoundedCornerShape(0.dp),
            s = RoundedCornerShape(0.dp),
        ),
        radius = MercadoPagoRadius(
            xxs = 0.dp,
            xs = 0.dp,
            s = 0.dp,
        ),
        outline = MercadoPagoOutline(
            xxs = 0.dp,
            xs = 0.dp,
        ),
        typography = MercadoPagoTypography(
            title = MercadoPagoTitleTypography(
                smallSemibold = TextStyle.Default,
            ),
            body = MercadoPagoBodyTypography(
                mediumSemibold = TextStyle.Default,
                mediumRegular = TextStyle.Default,
                smallSemibold = TextStyle.Default,
                smallRegular = TextStyle.Default,
                extraSmallSemibold = TextStyle.Default,
            ),
        ),
    )
}

/**
 * @suppress
 * Object that provides access to the current MercadoPago theme values.
 * This is the main entry point for accessing theme values in composables.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
object MercadoPagoTheme {
    /**
     * Gets the current color configuration from the theme.
     */
    val color: MercadoPagoColor
        @Composable
        get() = LocalMercadoPagoTheme.current.color

    /**
     * Gets the current spacing configuration from the theme.
     */
    val spacing: MercadoPagoSpacing
        @Composable
        get() = LocalMercadoPagoTheme.current.spacing

    /**
     * Gets the current shape configuration from the theme.
     */
    val shape: MercadoPagoShape
        @Composable
        get() = LocalMercadoPagoTheme.current.shape

    /**
     * Gets the current typography configuration from the theme.
     */
    val typography: MercadoPagoTypography
        @Composable
        get() = LocalMercadoPagoTheme.current.typography
}

/**
 * @suppress
 * Composable function that provides the MercadoPago theme to its content.
 * This is the main entry point for applying the theme to a composition.
 *
 * @param theme The theme scheme to be applied, defaults to [MercadoPagoDefaultThemes.Default]
 * @param appearance The appearance mode to be used, defaults to [MercadoPagoThemeAppearance.System]
 * @param content The content to be themed
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
@Composable
fun MercadoPagoTheme(
    theme: MercadoPagoThemeProviderScheme = MercadoPagoDefaultThemes.Default,
    appearance: MercadoPagoThemeAppearance = MercadoPagoThemeAppearance.System,
    content: @Composable () -> Unit,
) {
    val themeScheme = when (appearance) {
        MercadoPagoThemeAppearance.System -> if (isSystemInDarkTheme()) {
            theme.darkTheme
        } else {
            theme.lightTheme
        }
        MercadoPagoThemeAppearance.Light -> theme.lightTheme
        MercadoPagoThemeAppearance.Dark -> theme.darkTheme
    }
    CompositionLocalProvider(
        LocalMercadoPagoTheme provides themeScheme,
        content = content,
    )
}
