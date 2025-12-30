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
        andesColor = MercadoPagoAndesColor(
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
        andesSpacing = MercadoPagoAndesSpacing(
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
        andesShape = MercadoPagoAndesShape(
            none = RoundedCornerShape(0.dp),
            tiny = RoundedCornerShape(0.dp),
            xsmall = RoundedCornerShape(0.dp),
            small = RoundedCornerShape(0.dp),
            medium = RoundedCornerShape(0.dp),
            large = RoundedCornerShape(0.dp),
            xlarge = RoundedCornerShape(0.dp),
            full = RoundedCornerShape(0.dp),
        ),
        andesRadius = MercadoPagoAndesRadius(
            none = 0.dp,
            tiny = 0.dp,
            xsmall = 0.dp,
            small = 0.dp,
            medium = 0.dp,
            large = 0.dp,
            xlarge = 0.dp,
            full = 0.dp,
        ),
        andesBorderWidth = MercadoPagoAndesBorderWidth(
            none = 0.dp,
            small = 0.dp,
            medium = 0.dp,
            large = 0.dp,
            xlarge = 0.dp,
        ),
        andesTypography = MercadoPagoAndesTypography(
            heading = NewHeadingTypography(
                familyDefault = FontFamily.Default,
                size = NewTypographySize(
                    size10 = 0.sp,
                    size12 = 0.sp,
                    size14 = 0.sp,
                    size16 = 0.sp,
                    size18 = 0.sp,
                    size20 = 0.sp,
                    size24 = 0.sp,
                    size28 = 0.sp,
                    size32 = 0.sp,
                    size40 = 0.sp,
                    size48 = 0.sp,
                    size56 = 0.sp,
                ),
                lineHeight = NewTypographyLineHeight(
                    lineHeight12 = 0.sp,
                    lineHeight16 = 0.sp,
                    lineHeight18 = 0.sp,
                    lineHeight20 = 0.sp,
                    lineHeight22 = 0.sp,
                    lineHeight24 = 0.sp,
                    lineHeight28 = 0.sp,
                    lineHeight34 = 0.sp,
                    lineHeight40 = 0.sp,
                    lineHeight48 = 0.sp,
                    lineHeight56 = 0.sp,
                    lineHeight66 = 0.sp,
                ),
                weight = NewTypographyWeight(
                    regular = FontWeight.W400,
                    semibold = FontWeight.W600,
                    bold = FontWeight.W700,
                ),
                letterSpacing = NewTypographyLetterSpacing(
                    spacing0 = 0.sp,
                    spacingNegative1 = 0.sp,
                ),
                paragraphSpacing = NewTypographyParagraphSpacing(
                    spacing10 = 0.sp,
                    spacing12 = 0.sp,
                    spacing14 = 0.sp,
                    spacing16 = 0.sp,
                ),
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
     * Gets the current color configuration from the theme (legacy).
     */
    val color: MercadoPagoColor
        @Composable
        get() = LocalMercadoPagoTheme.current.color

    /**
     * Gets the current spacing configuration from the theme (legacy).
     */
    val spacing: MercadoPagoSpacing
        @Composable
        get() = LocalMercadoPagoTheme.current.spacing

    /**
     * Gets the current shape configuration from the theme (legacy).
     */
    val shape: MercadoPagoShape
        @Composable
        get() = LocalMercadoPagoTheme.current.shape

    /**
     * Gets the current typography configuration from the theme (legacy).
     */
    val typography: MercadoPagoTypography
        @Composable
        get() = LocalMercadoPagoTheme.current.typography
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
        get() = LocalMercadoPagoTheme.current.andesColor

    /**
     * Gets the current Andes spacing configuration from the theme.
     */
    val spacing: MercadoPagoAndesSpacing
        @Composable
        get() = LocalMercadoPagoTheme.current.andesSpacing

    /**
     * Gets the current Andes shape configuration from the theme.
     */
    val shape: MercadoPagoAndesShape
        @Composable
        get() = LocalMercadoPagoTheme.current.andesShape

    /**
     * Gets the current Andes radius configuration from the theme.
     */
    val radius: MercadoPagoAndesRadius
        @Composable
        get() = LocalMercadoPagoTheme.current.andesRadius

    /**
     * Gets the current Andes border width configuration from the theme.
     */
    val borderWidth: MercadoPagoAndesBorderWidth
        @Composable
        get() = LocalMercadoPagoTheme.current.andesBorderWidth

    /**
     * Gets the current Andes typography configuration from the theme.
     */
    val typography: MercadoPagoAndesTypography
        @Composable
        get() = LocalMercadoPagoTheme.current.andesTypography
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
