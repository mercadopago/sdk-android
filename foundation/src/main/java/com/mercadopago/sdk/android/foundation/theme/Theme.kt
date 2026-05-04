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
import com.mercadopago.sdk.android.foundation.color.MercadoPagoBackgroundColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoBorderColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoBrandColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoFeedbackColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoFeedbackTypeColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoFillColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoIconColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoInteractiveBorderColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoInteractiveColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoInteractiveFillColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoInteractiveIconColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoSurfaceColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoTextColor
import com.mercadopago.sdk.android.foundation.color.MercadoPagoTransparentColor
import com.mercadopago.sdk.android.foundation.outline.MercadoPagoBorderWidth
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoRadius
import com.mercadopago.sdk.android.foundation.shape.MercadoPagoShape
import com.mercadopago.sdk.android.foundation.spacing.MercadoPagoSpacing
import com.mercadopago.sdk.android.foundation.spacing.SpacingGap
import com.mercadopago.sdk.android.foundation.spacing.SpacingPaddings
import com.mercadopago.sdk.android.foundation.typography.BodyStyle
import com.mercadopago.sdk.android.foundation.typography.BodyTypography
import com.mercadopago.sdk.android.foundation.typography.HeadingStyle
import com.mercadopago.sdk.android.foundation.typography.HeadingTypography
import com.mercadopago.sdk.android.foundation.typography.MercadoPagoTypography
import com.mercadopago.sdk.android.foundation.typography.TitleTypography

/**
 * @suppress
 * CompositionLocal that provides the current MercadoPago theme configuration.
 * This is used to propagate theme values down the composition tree.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal val LocalMercadoPagoTheme = compositionLocalOf<MercadoPagoThemeProvider.Default> {
    MercadoPagoThemeProvider.Default(
        color = MercadoPagoColor(
            background = MercadoPagoBackgroundColor(
                primary = Color.Unspecified,
                secondary = Color.Unspecified,
            ),
            surface = MercadoPagoSurfaceColor(
                primaryIdle = Color.Unspecified,
                primaryActive = Color.Unspecified,
                primaryDisabled = Color.Unspecified,
            ),
            fill = MercadoPagoFillColor(
                primary = Color.Unspecified,
                secondary = Color.Unspecified,
                inverse = Color.Unspecified,
                disabled = Color.Unspecified,
                accentLoud = Color.Unspecified,
                accentQuiet = Color.Unspecified,
                defaultOnScroll = Color.Unspecified,
            ),
            border = MercadoPagoBorderColor(
                primary = Color.Unspecified,
                accent = Color.Unspecified,
                inverse = Color.Unspecified,
                disabled = Color.Unspecified,
            ),
            icon = MercadoPagoIconColor(
                primary = Color.Unspecified,
                secondary = Color.Unspecified,
                accent = Color.Unspecified,
                inverse = Color.Unspecified,
                disabled = Color.Unspecified,
            ),
            text = MercadoPagoTextColor(
                primary = Color.Unspecified,
                secondary = Color.Unspecified,
                accent = Color.Unspecified,
                inverse = Color.Unspecified,
                disabled = Color.Unspecified,
                linkIdle = Color.Unspecified,
                linkActive = Color.Unspecified,
            ),
            brand = MercadoPagoBrandColor(
                fillLoud = Color.Unspecified,
                fillQuiet = Color.Unspecified,
                gradientStart = Color.Unspecified,
                gradientEnd = Color.Unspecified,
            ),
            feedback = MercadoPagoFeedbackColor(
                informative = MercadoPagoFeedbackTypeColor(
                    fillLoud = Color.Unspecified,
                    fillQuiet = Color.Unspecified,
                    textLoud = Color.Unspecified,
                    borderLoud = Color.Unspecified,
                    iconLoud = Color.Unspecified,
                ),
                positive = MercadoPagoFeedbackTypeColor(
                    fillLoud = Color.Unspecified,
                    fillQuiet = Color.Unspecified,
                    textLoud = Color.Unspecified,
                    borderLoud = Color.Unspecified,
                    iconLoud = Color.Unspecified,
                ),
                caution = MercadoPagoFeedbackTypeColor(
                    fillLoud = Color.Unspecified,
                    fillQuiet = Color.Unspecified,
                    textLoud = Color.Unspecified,
                    borderLoud = Color.Unspecified,
                    iconLoud = Color.Unspecified,
                ),
                negative = MercadoPagoFeedbackTypeColor(
                    fillLoud = Color.Unspecified,
                    fillQuiet = Color.Unspecified,
                    textLoud = Color.Unspecified,
                    borderLoud = Color.Unspecified,
                    iconLoud = Color.Unspecified,
                ),
            ),
            interactive = MercadoPagoInteractiveColor(
                fillLoud = MercadoPagoInteractiveFillColor(
                    idle = Color.Unspecified,
                    hover = Color.Unspecified,
                    active = Color.Unspecified,
                ),
                fillQuiet = MercadoPagoInteractiveFillColor(
                    idle = Color.Unspecified,
                    hover = Color.Unspecified,
                    active = Color.Unspecified,
                ),
                fillMute = MercadoPagoInteractiveFillColor(
                    idle = Color.Unspecified,
                    hover = Color.Unspecified,
                    active = Color.Unspecified,
                ),
                border = MercadoPagoInteractiveBorderColor(
                    idle = Color.Unspecified,
                    active = Color.Unspecified,
                ),
                icon = MercadoPagoInteractiveIconColor(
                    idle = Color.Unspecified,
                    active = Color.Unspecified,
                    idleAccent = Color.Unspecified,
                    activeAccent = Color.Unspecified,
                ),
            ),
            transparent = MercadoPagoTransparentColor(
                transparent = Color.Unspecified,
            ),
        ),
        spacing = MercadoPagoSpacing(
            paddings = SpacingPaddings(
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
            gap = SpacingGap(
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
        shape = MercadoPagoShape(
            none = RoundedCornerShape(0.dp),
            tiny = RoundedCornerShape(0.dp),
            xsmall = RoundedCornerShape(0.dp),
            small = RoundedCornerShape(0.dp),
            medium = RoundedCornerShape(0.dp),
            large = RoundedCornerShape(0.dp),
            xlarge = RoundedCornerShape(0.dp),
            full = RoundedCornerShape(0.dp),
        ),
        radius = MercadoPagoRadius(
            none = 0.dp,
            tiny = 0.dp,
            xsmall = 0.dp,
            small = 0.dp,
            medium = 0.dp,
            large = 0.dp,
            xlarge = 0.dp,
            full = 0.dp,
        ),
        borderWidth = MercadoPagoBorderWidth(
            none = 0.dp,
            small = 0.dp,
            medium = 0.dp,
            large = 0.dp,
            xlarge = 0.dp,
        ),
        typography = MercadoPagoTypography(
            heading = HeadingTypography(
                default = HeadingStyle(
                    small = TextStyle.Default,
                    medium = TextStyle.Default,
                    huge = TextStyle.Default,
                ),
                narrow = HeadingStyle(
                    small = TextStyle.Default,
                    medium = TextStyle.Default,
                    huge = TextStyle.Default,
                ),
            ),
            body = BodyTypography(
                default = BodyStyle(
                    small = TextStyle.Default,
                    medium = TextStyle.Default,
                    large = TextStyle.Default,
                ),
                emphasis = BodyStyle(
                    small = TextStyle.Default,
                    medium = TextStyle.Default,
                    large = TextStyle.Default,
                ),
                textlink = BodyStyle(
                    small = TextStyle.Default,
                    medium = TextStyle.Default,
                    large = TextStyle.Default,
                ),
            ),
            title = TitleTypography(
                title = TextStyle.Default,
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
     * Gets the current radius configuration from the theme.
     */
    val radius: MercadoPagoRadius
        @Composable
        get() = LocalMercadoPagoTheme.current.radius

    /**
     * Gets the current border width configuration from the theme.
     */
    val borderWidth: MercadoPagoBorderWidth
        @Composable
        get() = LocalMercadoPagoTheme.current.borderWidth

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
 *
 * @param theme The theme configuration to be applied, defaults to [MercadoPagoThemes.Default]
 * @param style The user interface style to be used, defaults to [MercadoPagoUserInterfaceStyle.System]
 * @param content The content to be themed
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
@Composable
fun MercadoPagoTheme(
    theme: MercadoPagoThemeConfiguration = MercadoPagoThemes.Default,
    style: MercadoPagoUserInterfaceStyle = MercadoPagoUserInterfaceStyle.System,
    content: @Composable () -> Unit,
) {
    val themeScheme = when (style) {
        MercadoPagoUserInterfaceStyle.System -> if (isSystemInDarkTheme()) {
            theme.darkTheme
        } else {
            theme.lightTheme
        }
        MercadoPagoUserInterfaceStyle.Light -> theme.lightTheme
        MercadoPagoUserInterfaceStyle.Dark -> theme.darkTheme
    }
    CompositionLocalProvider(
        LocalMercadoPagoTheme provides themeScheme as MercadoPagoThemeProvider.Default,
        content = content,
    )
}
