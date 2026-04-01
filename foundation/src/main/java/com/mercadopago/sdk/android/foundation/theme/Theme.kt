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
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoAndesBorderWidth
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoAndesRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoAndesShape
import com.mercadopago.sdk.android.foundation.spacing.AndesSpacingGap
import com.mercadopago.sdk.android.foundation.spacing.AndesSpacingPaddings
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoAndesSpacing
import com.mercadopago.sdk.android.foundation.typography.AndesBodyStyle
import com.mercadopago.sdk.android.foundation.typography.AndesBodyTypography
import com.mercadopago.sdk.android.foundation.typography.AndesHeadingStyle
import com.mercadopago.sdk.android.foundation.typography.AndesHeadingTypography
import com.mercadopago.sdk.android.foundation.typography.AndesTitleTypography
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoAndesTypography

/**
 * @suppress
 * CompositionLocal that provides the current MercadoPago Andes theme configuration.
 * This is used to propagate Andes theme values down the composition tree.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal val LocalMercadoPagoAndesTheme = compositionLocalOf<MercadoPagoThemeProvider.Andes> {
    MercadoPagoThemeProvider.Andes(
        color = MercadoPagoAndesColor(
            background = MercadoPagoAndesBackgroundColor(
                primary = Color.Unspecified,
                secondary = Color.Unspecified,
            ),
            surface = MercadoPagoAndesSurfaceColor(
                primaryIdle = Color.Unspecified,
                primaryActive = Color.Unspecified,
                primaryDisabled = Color.Unspecified,
            ),
            fill = MercadoPagoAndesFillColor(
                primary = Color.Unspecified,
                secondary = Color.Unspecified,
                inverse = Color.Unspecified,
                disabled = Color.Unspecified,
                accentLoud = Color.Unspecified,
                accentQuiet = Color.Unspecified,
                defaultOnScroll = Color.Unspecified,
            ),
            border = MercadoPagoAndesBorderColor(
                primary = Color.Unspecified,
                accent = Color.Unspecified,
                inverse = Color.Unspecified,
                disabled = Color.Unspecified,
            ),
            icon = MercadoPagoAndesIconColor(
                primary = Color.Unspecified,
                secondary = Color.Unspecified,
                accent = Color.Unspecified,
                inverse = Color.Unspecified,
                disabled = Color.Unspecified,
            ),
            text = MercadoPagoAndesTextColor(
                primary = Color.Unspecified,
                secondary = Color.Unspecified,
                accent = Color.Unspecified,
                inverse = Color.Unspecified,
                disabled = Color.Unspecified,
                linkIdle = Color.Unspecified,
                linkActive = Color.Unspecified,
            ),
            brand = MercadoPagoAndesBrandColor(
                fillLoud = Color.Unspecified,
                fillQuiet = Color.Unspecified,
                gradientStart = Color.Unspecified,
                gradientEnd = Color.Unspecified,
            ),
            feedback = MercadoPagoAndesFeedbackColor(
                informative = MercadoPagoAndesFeedbackTypeColor(
                    fillLoud = Color.Unspecified,
                    fillQuiet = Color.Unspecified,
                    textLoud = Color.Unspecified,
                    borderLoud = Color.Unspecified,
                    iconLoud = Color.Unspecified,
                ),
                positive = MercadoPagoAndesFeedbackTypeColor(
                    fillLoud = Color.Unspecified,
                    fillQuiet = Color.Unspecified,
                    textLoud = Color.Unspecified,
                    borderLoud = Color.Unspecified,
                    iconLoud = Color.Unspecified,
                ),
                caution = MercadoPagoAndesFeedbackTypeColor(
                    fillLoud = Color.Unspecified,
                    fillQuiet = Color.Unspecified,
                    textLoud = Color.Unspecified,
                    borderLoud = Color.Unspecified,
                    iconLoud = Color.Unspecified,
                ),
                negative = MercadoPagoAndesFeedbackTypeColor(
                    fillLoud = Color.Unspecified,
                    fillQuiet = Color.Unspecified,
                    textLoud = Color.Unspecified,
                    borderLoud = Color.Unspecified,
                    iconLoud = Color.Unspecified,
                ),
            ),
            interactive = MercadoPagoAndesInteractiveColor(
                fillLoud = MercadoPagoAndesInteractiveFillColor(
                    idle = Color.Unspecified,
                    hover = Color.Unspecified,
                    active = Color.Unspecified,
                ),
                fillQuiet = MercadoPagoAndesInteractiveFillColor(
                    idle = Color.Unspecified,
                    hover = Color.Unspecified,
                    active = Color.Unspecified,
                ),
                fillMute = MercadoPagoAndesInteractiveFillColor(
                    idle = Color.Unspecified,
                    hover = Color.Unspecified,
                    active = Color.Unspecified,
                ),
                border = MercadoPagoAndesInteractiveBorderColor(
                    idle = Color.Unspecified,
                    active = Color.Unspecified,
                ),
                icon = MercadoPagoAndesInteractiveIconColor(
                    idle = Color.Unspecified,
                    active = Color.Unspecified,
                    idleAccent = Color.Unspecified,
                    activeAccent = Color.Unspecified,
                ),
            ),
            transparent = MercadoPagoAndesTransparentColor(
                transparent = Color.Unspecified,
            ),
        ),
        spacing = MercadoPagoAndesSpacing(
            paddings = AndesSpacingPaddings(
                none = 0.dp,
                pico = 0.dp,
                xnano = 0.dp,
                nano = 0.dp,
                xmicro = 0.dp,
                micro = 0.dp,
                xtiny = 0.dp,
                tiny = 0.dp,
                xsmall = 0.dp,
                small = 0.dp,
                medium = 0.dp,
                large = 0.dp,
                xlarge = 0.dp,
                huge = 0.dp,
                xhuge = 0.dp,
                mega = 0.dp,
                xmega = 0.dp,
            ),
            gap = AndesSpacingGap(
                none = 0.dp,
                pico = 0.dp,
                xnano = 0.dp,
                nano = 0.dp,
                xmicro = 0.dp,
                micro = 0.dp,
                xtiny = 0.dp,
                tiny = 0.dp,
                xsmall = 0.dp,
                small = 0.dp,
                medium = 0.dp,
                large = 0.dp,
                xlarge = 0.dp,
                huge = 0.dp,
                xhuge = 0.dp,
                mega = 0.dp,
                xmega = 0.dp,
            ),
        ),
        shape = MercadoPagoAndesShape(
            none = RoundedCornerShape(0.dp),
            tiny = RoundedCornerShape(0.dp),
            xsmall = RoundedCornerShape(0.dp),
            small = RoundedCornerShape(0.dp),
            medium = RoundedCornerShape(0.dp),
            large = RoundedCornerShape(0.dp),
            xlarge = RoundedCornerShape(0.dp),
            full = RoundedCornerShape(0.dp),
        ),
        radius = MercadoPagoAndesRadius(
            none = 0.dp,
            tiny = 0.dp,
            xsmall = 0.dp,
            small = 0.dp,
            medium = 0.dp,
            large = 0.dp,
            xlarge = 0.dp,
            full = 0.dp,
        ),
        borderWidth = MercadoPagoAndesBorderWidth(
            none = 0.dp,
            small = 0.dp,
            medium = 0.dp,
            large = 0.dp,
            xlarge = 0.dp,
        ),
        typography = MercadoPagoAndesTypography(
            heading = AndesHeadingTypography(
                default = AndesHeadingStyle(
                    small = TextStyle.Default,
                    medium = TextStyle.Default,
                    huge = TextStyle.Default,
                ),
                narrow = AndesHeadingStyle(
                    small = TextStyle.Default,
                    medium = TextStyle.Default,
                    huge = TextStyle.Default,
                ),
            ),
            body = AndesBodyTypography(
                default = AndesBodyStyle(
                    small = TextStyle.Default,
                    medium = TextStyle.Default,
                    large = TextStyle.Default,
                ),
                emphasis = AndesBodyStyle(
                    small = TextStyle.Default,
                    medium = TextStyle.Default,
                    large = TextStyle.Default,
                ),
                textlink = AndesBodyStyle(
                    small = TextStyle.Default,
                    medium = TextStyle.Default,
                    large = TextStyle.Default,
                ),
            ),
            title = AndesTitleTypography(
                title = TextStyle.Default,
            ),
        ),
    )
}

/**
 * @suppress
 * Object that provides access to the current MercadoPago Andes theme values.
 * This is the main entry point for accessing Andes theme values in composables.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
object MercadoPagoAndesTheme {
    /**
     * Gets the current Andes color configuration from the theme.
     */
    val color: MercadoPagoAndesColor
        @Composable
        get() = LocalMercadoPagoAndesTheme.current.color

    /**
     * Gets the current Andes spacing configuration from the theme.
     */
    val spacing: MercadoPagoAndesSpacing
        @Composable
        get() = LocalMercadoPagoAndesTheme.current.spacing

    /**
     * Gets the current Andes shape configuration from the theme.
     */
    val shape: MercadoPagoAndesShape
        @Composable
        get() = LocalMercadoPagoAndesTheme.current.shape

    /**
     * Gets the current Andes radius configuration from the theme.
     */
    val radius: MercadoPagoAndesRadius
        @Composable
        get() = LocalMercadoPagoAndesTheme.current.radius

    /**
     * Gets the current Andes border width configuration from the theme.
     */
    val borderWidth: MercadoPagoAndesBorderWidth
        @Composable
        get() = LocalMercadoPagoAndesTheme.current.borderWidth

    /**
     * Gets the current Andes typography configuration from the theme.
     */
    val typography: MercadoPagoAndesTypography
        @Composable
        get() = LocalMercadoPagoAndesTheme.current.typography
}

/**
 * @suppress
 * Composable function that provides the MercadoPago theme to its content.
 *
 * @param theme The theme scheme to be applied, defaults to [MercadoPagoThemes.Andes]
 * @param appearance The appearance mode to be used, defaults to [MercadoPagoThemeAppearance.System]
 * @param content The content to be themed
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
@Composable
fun MercadoPagoTheme(
    theme: MercadoPagoThemeProviderScheme = MercadoPagoThemes.Andes,
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
        LocalMercadoPagoAndesTheme provides themeScheme as MercadoPagoThemeProvider.Andes,
        content = content,
    )
}
