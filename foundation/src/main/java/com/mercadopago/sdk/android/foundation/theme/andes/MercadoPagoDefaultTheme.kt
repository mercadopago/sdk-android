package com.mercadopago.sdk.android.foundation.theme.andes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseColor
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
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeProvider
import com.mercadopago.sdk.android.foundation.typography.DefaultTypography

private const val COLOR_GROUP_BACKGROUND = "Background"
private const val COLOR_GROUP_SURFACE = "Surface"
private const val COLOR_GROUP_FILL = "Fill"
private const val COLOR_GROUP_BORDER = "Border"
private const val COLOR_GROUP_ICON = "Icon"
private const val COLOR_GROUP_TEXT = "Text"
private const val COLOR_GROUP_BRAND = "Brand"
private const val COLOR_GROUP_FEEDBACK = "Feedback"
private const val COLOR_GROUP_INTERACTIVE = "Interactive"

// Background Colors
@ShowkaseColor(name = "Background Primary", group = COLOR_GROUP_BACKGROUND)
internal val DefaultBackgroundPrimary = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Background Secondary", group = COLOR_GROUP_BACKGROUND)
internal val DefaultBackgroundSecondary = Color(0xFFE7E9F3)

// Surface Colors
@ShowkaseColor(name = "Surface Primary Idle", group = COLOR_GROUP_SURFACE)
internal val DefaultSurfacePrimaryIdle = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Surface Primary Active", group = COLOR_GROUP_SURFACE)
internal val DefaultSurfacePrimaryActive = Color(0xFFE7E9F3)

@ShowkaseColor(name = "Surface Primary Disabled", group = COLOR_GROUP_SURFACE)
internal val DefaultSurfacePrimaryDisabled = Color(0x00FFFFFF)

// Fill Colors
@ShowkaseColor(name = "Fill Primary", group = COLOR_GROUP_FILL)
internal val DefaultFillPrimary = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Fill Secondary", group = COLOR_GROUP_FILL)
internal val DefaultFillSecondary = Color(0xFFD0D4E6)

@ShowkaseColor(name = "Fill Inverse", group = COLOR_GROUP_FILL)
internal val DefaultFillInverse = Color(0xFF282834)

@ShowkaseColor(name = "Fill Disabled", group = COLOR_GROUP_FILL)
internal val DefaultFillDisabled = Color(0xFFD0D4E6)

@ShowkaseColor(name = "Fill Accent Loud", group = COLOR_GROUP_FILL)
internal val DefaultFillAccentLoud = Color(0xFF434CE4)

@ShowkaseColor(name = "Fill Accent Quiet", group = COLOR_GROUP_FILL)
internal val DefaultFillAccentQuiet = Color(0xFFE9F1FF)

@ShowkaseColor(name = "Fill Default On Scroll", group = COLOR_GROUP_FILL)
internal val DefaultFillDefaultOnScroll = Color(0x99FFFFFF)

// Border Colors
@ShowkaseColor(name = "Border Primary", group = COLOR_GROUP_BORDER)
internal val DefaultBorderPrimary = Color(0xFFD0D4E6)

@ShowkaseColor(name = "Border Accent", group = COLOR_GROUP_BORDER)
internal val DefaultBorderAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "Border Inverse", group = COLOR_GROUP_BORDER)
internal val DefaultBorderInverse = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Border Disabled", group = COLOR_GROUP_BORDER)
internal val DefaultBorderDisabled = Color(0xFFB5B9D4)

// Icon Colors
@ShowkaseColor(name = "Icon Primary", group = COLOR_GROUP_ICON)
internal val DefaultIconPrimary = Color(0xFF282834)

@ShowkaseColor(name = "Icon Secondary", group = COLOR_GROUP_ICON)
internal val DefaultIconSecondary = Color(0xFF646587)

@ShowkaseColor(name = "Icon Accent", group = COLOR_GROUP_ICON)
internal val DefaultIconAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "Icon Inverse", group = COLOR_GROUP_ICON)
internal val DefaultIconInverse = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Icon Disabled", group = COLOR_GROUP_ICON)
internal val DefaultIconDisabled = Color(0xFF9C9EBF)

// Text Colors
@ShowkaseColor(name = "Text Primary", group = COLOR_GROUP_TEXT)
internal val DefaultTextPrimary = Color(0xFF282834)

@ShowkaseColor(name = "Text Secondary", group = COLOR_GROUP_TEXT)
internal val DefaultTextSecondary = Color(0xFF646587)

@ShowkaseColor(name = "Text Accent", group = COLOR_GROUP_TEXT)
internal val DefaultTextAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "Text Inverse", group = COLOR_GROUP_TEXT)
internal val DefaultTextInverse = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Text Disabled", group = COLOR_GROUP_TEXT)
internal val DefaultTextDisabled = Color(0xFF9C9EBF)

@ShowkaseColor(name = "Text Link Idle", group = COLOR_GROUP_TEXT)
internal val DefaultTextLinkIdle = Color(0xFF434CE4)

@ShowkaseColor(name = "Text Link Active", group = COLOR_GROUP_TEXT)
internal val DefaultTextLinkActive = Color(0xFF272C96)

// Brand Colors
@ShowkaseColor(name = "Brand Fill Loud", group = COLOR_GROUP_BRAND)
internal val DefaultBrandFillLoud = Color(0xFFFFE600)

@ShowkaseColor(name = "Brand Fill Quiet", group = COLOR_GROUP_BRAND)
internal val DefaultBrandFillQuiet = Color(0xFFFFF394)

@ShowkaseColor(name = "Brand Gradient Start", group = COLOR_GROUP_BRAND)
internal val DefaultBrandGradientStart = Color(0xFFF9C200)

@ShowkaseColor(name = "Brand Gradient End", group = COLOR_GROUP_BRAND)
internal val DefaultBrandGradientEnd = Color(0xFFFFE600)

// Feedback Informative Colors
@ShowkaseColor(name = "Feedback Informative Fill Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackInformativeFillLoud = Color(0xFF434CE4)

@ShowkaseColor(name = "Feedback Informative Fill Quiet", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackInformativeFillQuiet = Color(0xFFE9F1FF)

@ShowkaseColor(name = "Feedback Informative Text Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackInformativeTextLoud = Color(0xFF434CE4)

@ShowkaseColor(name = "Feedback Informative Border Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackInformativeBorderLoud = Color(0xFF5C70FA)

@ShowkaseColor(name = "Feedback Informative Icon Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackInformativeIconLoud = Color(0xFF434CE4)

// Feedback Positive Colors
@ShowkaseColor(name = "Feedback Positive Fill Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackPositiveFillLoud = Color(0xFF1F8923)

@ShowkaseColor(name = "Feedback Positive Fill Quiet", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackPositiveFillQuiet = Color(0xFFDEFADE)

@ShowkaseColor(name = "Feedback Positive Text Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackPositiveTextLoud = Color(0xFF1F8923)

@ShowkaseColor(name = "Feedback Positive Border Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackPositiveBorderLoud = Color(0xFF14A919)

@ShowkaseColor(name = "Feedback Positive Icon Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackPositiveIconLoud = Color(0xFF1F8923)

// Feedback Caution Colors
@ShowkaseColor(name = "Feedback Caution Fill Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackCautionFillLoud = Color(0xFFD74009)

@ShowkaseColor(name = "Feedback Caution Fill Quiet", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackCautionFillQuiet = Color(0xFFFFEDC7)

@ShowkaseColor(name = "Feedback Caution Text Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackCautionTextLoud = Color(0xFFD74009)

@ShowkaseColor(name = "Feedback Caution Border Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackCautionBorderLoud = Color(0xFFF05705)

@ShowkaseColor(name = "Feedback Caution Icon Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackCautionIconLoud = Color(0xFFD74009)

// Feedback Negative Colors
@ShowkaseColor(name = "Feedback Negative Fill Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackNegativeFillLoud = Color(0xFFC4031D)

@ShowkaseColor(name = "Feedback Negative Fill Quiet", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackNegativeFillQuiet = Color(0xFFFFE5E9)

@ShowkaseColor(name = "Feedback Negative Text Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackNegativeTextLoud = Color(0xFFC4031D)

@ShowkaseColor(name = "Feedback Negative Border Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackNegativeBorderLoud = Color(0xFFED314A)

@ShowkaseColor(name = "Feedback Negative Icon Loud", group = COLOR_GROUP_FEEDBACK)
internal val DefaultFeedbackNegativeIconLoud = Color(0xFFC4031D)

// Interactive Fill Loud Colors
@ShowkaseColor(name = "Interactive Fill Loud Idle", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveFillLoudIdle = Color(0xFF434CE4)

@ShowkaseColor(name = "Interactive Fill Loud Hover", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveFillLoudHover = Color(0xFF353AC5)

@ShowkaseColor(name = "Interactive Fill Loud Active", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveFillLoudActive = Color(0xFF272C96)

// Interactive Fill Quiet Colors
@ShowkaseColor(name = "Interactive Fill Quiet Idle", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveFillQuietIdle = Color(0xFFE9F1FF)

@ShowkaseColor(name = "Interactive Fill Quiet Hover", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveFillQuietHover = Color(0xFFDEE9FF)

@ShowkaseColor(name = "Interactive Fill Quiet Active", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveFillQuietActive = Color(0xFFC6D8FF)

// Interactive Fill Mute Colors
@ShowkaseColor(name = "Interactive Fill Mute Idle", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveFillMuteIdle = Color(0x00FFFFFF)

@ShowkaseColor(name = "Interactive Fill Mute Hover", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveFillMuteHover = Color(0xFFE9F1FF)

@ShowkaseColor(name = "Interactive Fill Mute Active", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveFillMuteActive = Color(0xFFDEE9FF)

// Interactive Border Colors
@ShowkaseColor(name = "Interactive Border Idle", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveBorderIdle = Color(0xFF8788AB)

@ShowkaseColor(name = "Interactive Border Active", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveBorderActive = Color(0xFF434CE4)

// Interactive Icon Colors
@ShowkaseColor(name = "Interactive Icon Idle", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveIconIdle = Color(0xFF646587)

@ShowkaseColor(name = "Interactive Icon Active", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveIconActive = Color(0xFF282834)

@ShowkaseColor(name = "Interactive Icon Idle Accent", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveIconIdleAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "Interactive Icon Active Accent", group = COLOR_GROUP_INTERACTIVE)
internal val DefaultInteractiveIconActiveAccent = Color(0xFF272C96)

// Transparent Color
internal val DefaultTransparent = Color(0x00FFFFFF)

// Andes Default Colors Configuration
internal val DefaultLightColors = MercadoPagoColor(
    background = MercadoPagoBackgroundColor(
        primary = DefaultBackgroundPrimary,
        secondary = DefaultBackgroundSecondary,
    ),
    surface = MercadoPagoSurfaceColor(
        primaryIdle = DefaultSurfacePrimaryIdle,
        primaryActive = DefaultSurfacePrimaryActive,
        primaryDisabled = DefaultSurfacePrimaryDisabled,
    ),
    fill = MercadoPagoFillColor(
        primary = DefaultFillPrimary,
        secondary = DefaultFillSecondary,
        inverse = DefaultFillInverse,
        disabled = DefaultFillDisabled,
        accentLoud = DefaultFillAccentLoud,
        accentQuiet = DefaultFillAccentQuiet,
        defaultOnScroll = DefaultFillDefaultOnScroll,
    ),
    border = MercadoPagoBorderColor(
        primary = DefaultBorderPrimary,
        accent = DefaultBorderAccent,
        inverse = DefaultBorderInverse,
        disabled = DefaultBorderDisabled,
    ),
    icon = MercadoPagoIconColor(
        primary = DefaultIconPrimary,
        secondary = DefaultIconSecondary,
        accent = DefaultIconAccent,
        inverse = DefaultIconInverse,
        disabled = DefaultIconDisabled,
    ),
    text = MercadoPagoTextColor(
        primary = DefaultTextPrimary,
        secondary = DefaultTextSecondary,
        accent = DefaultTextAccent,
        inverse = DefaultTextInverse,
        disabled = DefaultTextDisabled,
        linkIdle = DefaultTextLinkIdle,
        linkActive = DefaultTextLinkActive,
    ),
    brand = MercadoPagoBrandColor(
        fillLoud = DefaultBrandFillLoud,
        fillQuiet = DefaultBrandFillQuiet,
        gradientStart = DefaultBrandGradientStart,
        gradientEnd = DefaultBrandGradientEnd,
    ),
    feedback = MercadoPagoFeedbackColor(
        informative = MercadoPagoFeedbackTypeColor(
            fillLoud = DefaultFeedbackInformativeFillLoud,
            fillQuiet = DefaultFeedbackInformativeFillQuiet,
            textLoud = DefaultFeedbackInformativeTextLoud,
            borderLoud = DefaultFeedbackInformativeBorderLoud,
            iconLoud = DefaultFeedbackInformativeIconLoud,
        ),
        positive = MercadoPagoFeedbackTypeColor(
            fillLoud = DefaultFeedbackPositiveFillLoud,
            fillQuiet = DefaultFeedbackPositiveFillQuiet,
            textLoud = DefaultFeedbackPositiveTextLoud,
            borderLoud = DefaultFeedbackPositiveBorderLoud,
            iconLoud = DefaultFeedbackPositiveIconLoud,
        ),
        caution = MercadoPagoFeedbackTypeColor(
            fillLoud = DefaultFeedbackCautionFillLoud,
            fillQuiet = DefaultFeedbackCautionFillQuiet,
            textLoud = DefaultFeedbackCautionTextLoud,
            borderLoud = DefaultFeedbackCautionBorderLoud,
            iconLoud = DefaultFeedbackCautionIconLoud,
        ),
        negative = MercadoPagoFeedbackTypeColor(
            fillLoud = DefaultFeedbackNegativeFillLoud,
            fillQuiet = DefaultFeedbackNegativeFillQuiet,
            textLoud = DefaultFeedbackNegativeTextLoud,
            borderLoud = DefaultFeedbackNegativeBorderLoud,
            iconLoud = DefaultFeedbackNegativeIconLoud,
        ),
    ),
    interactive = MercadoPagoInteractiveColor(
        fillLoud = MercadoPagoInteractiveFillColor(
            idle = DefaultInteractiveFillLoudIdle,
            hover = DefaultInteractiveFillLoudHover,
            active = DefaultInteractiveFillLoudActive,
        ),
        fillQuiet = MercadoPagoInteractiveFillColor(
            idle = DefaultInteractiveFillQuietIdle,
            hover = DefaultInteractiveFillQuietHover,
            active = DefaultInteractiveFillQuietActive,
        ),
        fillMute = MercadoPagoInteractiveFillColor(
            idle = DefaultInteractiveFillMuteIdle,
            hover = DefaultInteractiveFillMuteHover,
            active = DefaultInteractiveFillMuteActive,
        ),
        border = MercadoPagoInteractiveBorderColor(
            idle = DefaultInteractiveBorderIdle,
            active = DefaultInteractiveBorderActive,
        ),
        icon = MercadoPagoInteractiveIconColor(
            idle = DefaultInteractiveIconIdle,
            active = DefaultInteractiveIconActive,
            idleAccent = DefaultInteractiveIconIdleAccent,
            activeAccent = DefaultInteractiveIconActiveAccent,
        ),
    ),
    transparent = MercadoPagoTransparentColor(
        transparent = DefaultTransparent,
    ),
)

// New Default Spacing Configuration
internal val NewDefaultLightSpacingPaddings = SpacingPaddings(
    none = 0.dp,
    pico = 2.dp,
    xnano = 4.dp,
    nano = 6.dp,
    xmicro = 8.dp,
    micro = 12.dp,
    xtiny = 16.dp,
    tiny = 20.dp,
    xsmall = 24.dp,
    small = 32.dp,
    medium = 40.dp,
    large = 48.dp,
    xlarge = 56.dp,
    huge = 64.dp,
    xhuge = 72.dp,
    mega = 80.dp,
    xmega = 84.dp,
)

internal val NewDefaultLightSpacingGap = SpacingGap(
    none = 0.dp,
    pico = 2.dp,
    xnano = 4.dp,
    nano = 6.dp,
    xmicro = 8.dp,
    micro = 12.dp,
    xtiny = 16.dp,
    tiny = 20.dp,
    xsmall = 24.dp,
    small = 32.dp,
    medium = 40.dp,
    large = 48.dp,
    xlarge = 56.dp,
    huge = 64.dp,
    xhuge = 72.dp,
    mega = 80.dp,
    xmega = 84.dp,
)

internal val DefaultLightSpacing = MercadoPagoSpacing(
    paddings = NewDefaultLightSpacingPaddings,
    gap = NewDefaultLightSpacingGap,
)

// New Default Radius Configuration
internal val DefaultLightRadius = MercadoPagoRadius(
    none = 0.dp,
    tiny = 4.dp,
    xsmall = 6.dp,
    small = 8.dp,
    medium = 12.dp,
    large = 16.dp,
    xlarge = 20.dp,
    full = 9999.dp,
)

// New Default Shape Configuration
internal val DefaultLightShape = MercadoPagoShape(
    none = RoundedCornerShape(0.dp),
    tiny = RoundedCornerShape(4.dp),
    xsmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    xlarge = RoundedCornerShape(20.dp),
    full = RoundedCornerShape(9999.dp),
)

// New Default Border Width Configuration
internal val DefaultLightBorderWidth = MercadoPagoBorderWidth(
    none = 0.dp,
    small = 1.dp,
    medium = 2.dp,
    large = 3.dp,
    xlarge = 4.dp,
)

// New Default Typography Configuration
internal val InterFontFamily = FontFamily.Default

internal val MercadoPagoDefaultLightTheme = MercadoPagoThemeProvider.Default(
    color = DefaultLightColors,
    spacing = DefaultLightSpacing,
    shape = DefaultLightShape,
    radius = DefaultLightRadius,
    borderWidth = DefaultLightBorderWidth,
    typography = DefaultTypography,
)
