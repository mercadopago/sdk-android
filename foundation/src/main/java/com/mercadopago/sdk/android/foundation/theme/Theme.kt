package com.mercadopago.sdk.android.foundation.theme

import androidx.annotation.RestrictTo
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mercadopago.sdk.android.foundation.color.BackgroundColor
import com.mercadopago.sdk.android.foundation.color.FeedbackColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesBackgroundColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesBorderColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesBrandColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesFeedbackColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesFeedbackTypeColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesFillColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesIconColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesInteractiveBorderColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesInteractiveColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesInteractiveFillColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesInteractiveIconColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesSurfaceColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesTextColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoAndesTransparentColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoColor
import com.mercadopago.sdk.android.foundation.color.OutlineColor
import com.mercadopago.sdk.android.foundation.color.TextColor
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoAndesBorderWidth
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoOutline
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoAndesRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoAndesShape
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoShape
import com.mercadopago.sdk.android.foundation.spacing.AndesSpacingGap
import com.mercadopago.sdk.android.foundation.spacing.AndesSpacingPaddings
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoAndesSpacing
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoSpacing
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoAndesTypography
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoBodyTypography
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoTitleTypography
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoTypography
import com.mercadopago.sdk.android.foundation.typography.NewHeadingTypography
import com.mercadopago.sdk.android.foundation.typography.NewTypographyLetterSpacing
import com.mercadopago.sdk.android.foundation.typography.NewTypographyLineHeight
import com.mercadopago.sdk.android.foundation.typography.NewTypographyParagraphSpacing
import com.mercadopago.sdk.android.foundation.typography.NewTypographySize
import com.mercadopago.sdk.android.foundation.typography.NewTypographyWeight

/**
 * @suppress
 * CompositionLocal that provides the current MercadoPago theme configuration.
 * This is used to propagate theme values down the composition tree.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal val LocalMercadoPagoTheme = compositionLocalOf<MercadoPagoThemeProvider> {
    MercadoPagoThemeProvider.Legacy(
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
 * Object that provides access to the current MercadoPago legacy theme values.
 * This is the main entry point for accessing legacy theme values in composables.
 * Use this object when working with the legacy design system.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
object MercadoPagoTheme {
    /**
     * Gets the current color configuration from the theme (legacy).
     */
    val color: MercadoPagoColor
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> theme.color
            is MercadoPagoThemeProvider.Andes -> throw IllegalStateException("Cannot access legacy color from Andes theme. Use MercadoPagoAndesTheme instead.")
        }

    /**
     * Gets the current spacing configuration from the theme (legacy).
     */
    val spacing: MercadoPagoSpacing
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> theme.spacing
            is MercadoPagoThemeProvider.Andes -> throw IllegalStateException("Cannot access legacy spacing from Andes theme. Use MercadoPagoAndesTheme instead.")
        }

    /**
     * Gets the current shape configuration from the theme (legacy).
     */
    val shape: MercadoPagoShape
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> theme.shape
            is MercadoPagoThemeProvider.Andes -> throw IllegalStateException("Cannot access legacy shape from Andes theme. Use MercadoPagoAndesTheme instead.")
        }

    /**
     * Gets the current radius configuration from the theme (legacy).
     */
    val radius: MercadoPagoRadius
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> theme.radius
            is MercadoPagoThemeProvider.Andes -> throw IllegalStateException("Cannot access legacy radius from Andes theme. Use MercadoPagoAndesTheme instead.")
        }

    /**
     * Gets the current outline configuration from the theme (legacy).
     */
    val outline: MercadoPagoOutline
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> theme.outline
            is MercadoPagoThemeProvider.Andes -> throw IllegalStateException("Cannot access legacy outline from Andes theme. Use MercadoPagoAndesTheme instead.")
        }

    /**
     * Gets the current typography configuration from the theme (legacy).
     */
    val typography: MercadoPagoTypography
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> theme.typography
            is MercadoPagoThemeProvider.Andes -> throw IllegalStateException("Cannot access legacy typography from Andes theme. Use MercadoPagoAndesTheme instead.")
        }
}

/**
 * @suppress
 * Object that provides access to the current MercadoPago Andes theme values.
 * This is the main entry point for accessing Andes theme values in composables.
 * Use this object when working with the Andes design system.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
object MercadoPagoAndesTheme {
    /**
     * Gets the current Andes color configuration from the theme.
     */
    val color: MercadoPagoAndesColor
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> throw IllegalStateException("Cannot access Andes color from legacy theme. Use MercadoPagoTheme instead.")
            is MercadoPagoThemeProvider.Andes -> theme.color
        }

    /**
     * Gets the current Andes spacing configuration from the theme.
     */
    val spacing: MercadoPagoAndesSpacing
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> throw IllegalStateException("Cannot access Andes spacing from legacy theme. Use MercadoPagoTheme instead.")
            is MercadoPagoThemeProvider.Andes -> theme.spacing
        }

    /**
     * Gets the current Andes shape configuration from the theme.
     */
    val shape: MercadoPagoAndesShape
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> throw IllegalStateException("Cannot access Andes shape from legacy theme. Use MercadoPagoTheme instead.")
            is MercadoPagoThemeProvider.Andes -> theme.shape
        }

    /**
     * Gets the current Andes radius configuration from the theme.
     */
    val radius: MercadoPagoAndesRadius
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> throw IllegalStateException("Cannot access Andes radius from legacy theme. Use MercadoPagoTheme instead.")
            is MercadoPagoThemeProvider.Andes -> theme.radius
        }

    /**
     * Gets the current Andes border width configuration from the theme.
     */
    val borderWidth: MercadoPagoAndesBorderWidth
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> throw IllegalStateException("Cannot access Andes borderWidth from legacy theme. Use MercadoPagoTheme instead.")
            is MercadoPagoThemeProvider.Andes -> theme.borderWidth
        }

    /**
     * Gets the current Andes typography configuration from the theme.
     */
    val typography: MercadoPagoAndesTypography
        @Composable
        get() = when (val theme = LocalMercadoPagoTheme.current) {
            is MercadoPagoThemeProvider.Legacy -> throw IllegalStateException("Cannot access Andes typography from legacy theme. Use MercadoPagoTheme instead.")
            is MercadoPagoThemeProvider.Andes -> theme.typography
        }
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
