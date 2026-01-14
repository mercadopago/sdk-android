package com.mercadopago.sdk.android.foundation.theme.andes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseColor
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
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeProvider
import com.mercadopago.sdk.android.foundation.typography.AndesDefaultTypography

private const val ANDES_COLOR_GROUP_BACKGROUND = "Andes Background"
private const val ANDES_COLOR_GROUP_SURFACE = "Andes Surface"
private const val ANDES_COLOR_GROUP_FILL = "Andes Fill"
private const val ANDES_COLOR_GROUP_BORDER = "Andes Border"
private const val ANDES_COLOR_GROUP_ICON = "Andes Icon"
private const val ANDES_COLOR_GROUP_TEXT = "Andes Text"
private const val ANDES_COLOR_GROUP_BRAND = "Andes Brand"
private const val ANDES_COLOR_GROUP_FEEDBACK = "Andes Feedback"
private const val ANDES_COLOR_GROUP_INTERACTIVE = "Andes Interactive"

// Background Colors
@ShowkaseColor(name = "Andes Background Primary", group = ANDES_COLOR_GROUP_BACKGROUND)
internal val AndesDefaultBackgroundPrimary = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Andes Background Secondary", group = ANDES_COLOR_GROUP_BACKGROUND)
internal val AndesDefaultBackgroundSecondary = Color(0xFFE7E9F3)

// Surface Colors
@ShowkaseColor(name = "Andes Surface Primary Idle", group = ANDES_COLOR_GROUP_SURFACE)
internal val AndesDefaultSurfacePrimaryIdle = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Andes Surface Primary Active", group = ANDES_COLOR_GROUP_SURFACE)
internal val AndesDefaultSurfacePrimaryActive = Color(0xFFE7E9F3)

@ShowkaseColor(name = "Andes Surface Primary Disabled", group = ANDES_COLOR_GROUP_SURFACE)
internal val AndesDefaultSurfacePrimaryDisabled = Color(0x00FFFFFF)

// Fill Colors
@ShowkaseColor(name = "Andes Fill Primary", group = ANDES_COLOR_GROUP_FILL)
internal val AndesDefaultFillPrimary = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Andes Fill Secondary", group = ANDES_COLOR_GROUP_FILL)
internal val AndesDefaultFillSecondary = Color(0xFFD0D4E6)

@ShowkaseColor(name = "Andes Fill Inverse", group = ANDES_COLOR_GROUP_FILL)
internal val AndesDefaultFillInverse = Color(0xFF282834)

@ShowkaseColor(name = "Andes Fill Disabled", group = ANDES_COLOR_GROUP_FILL)
internal val AndesDefaultFillDisabled = Color(0xFFD0D4E6)

@ShowkaseColor(name = "Andes Fill Accent Loud", group = ANDES_COLOR_GROUP_FILL)
internal val AndesDefaultFillAccentLoud = Color(0xFF434CE4)

@ShowkaseColor(name = "Andes Fill Accent Quiet", group = ANDES_COLOR_GROUP_FILL)
internal val AndesDefaultFillAccentQuiet = Color(0xFFE9F1FF)

@ShowkaseColor(name = "Andes Fill Default On Scroll", group = ANDES_COLOR_GROUP_FILL)
internal val AndesDefaultFillDefaultOnScroll = Color(0x99FFFFFF)

// Border Colors
@ShowkaseColor(name = "Andes Border Primary", group = ANDES_COLOR_GROUP_BORDER)
internal val AndesDefaultBorderPrimary = Color(0xFFD0D4E6)

@ShowkaseColor(name = "Andes Border Accent", group = ANDES_COLOR_GROUP_BORDER)
internal val AndesDefaultBorderAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "Andes Border Inverse", group = ANDES_COLOR_GROUP_BORDER)
internal val AndesDefaultBorderInverse = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Andes Border Disabled", group = ANDES_COLOR_GROUP_BORDER)
internal val AndesDefaultBorderDisabled = Color(0xFFB5B9D4)

// Icon Colors
@ShowkaseColor(name = "Andes Icon Primary", group = ANDES_COLOR_GROUP_ICON)
internal val AndesDefaultIconPrimary = Color(0xFF282834)

@ShowkaseColor(name = "Andes Icon Secondary", group = ANDES_COLOR_GROUP_ICON)
internal val AndesDefaultIconSecondary = Color(0xFF646587)

@ShowkaseColor(name = "Andes Icon Accent", group = ANDES_COLOR_GROUP_ICON)
internal val AndesDefaultIconAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "Andes Icon Inverse", group = ANDES_COLOR_GROUP_ICON)
internal val AndesDefaultIconInverse = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Andes Icon Disabled", group = ANDES_COLOR_GROUP_ICON)
internal val AndesDefaultIconDisabled = Color(0xFF9C9EBF)

// Text Colors
@ShowkaseColor(name = "Andes Text Primary", group = ANDES_COLOR_GROUP_TEXT)
internal val AndesDefaultTextPrimary = Color(0xFF282834)

@ShowkaseColor(name = "Andes Text Secondary", group = ANDES_COLOR_GROUP_TEXT)
internal val AndesDefaultTextSecondary = Color(0xFF646587)

@ShowkaseColor(name = "Andes Text Accent", group = ANDES_COLOR_GROUP_TEXT)
internal val AndesDefaultTextAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "Andes Text Inverse", group = ANDES_COLOR_GROUP_TEXT)
internal val AndesDefaultTextInverse = Color(0xFFFFFFFF)

@ShowkaseColor(name = "Andes Text Disabled", group = ANDES_COLOR_GROUP_TEXT)
internal val AndesDefaultTextDisabled = Color(0xFF9C9EBF)

@ShowkaseColor(name = "Andes Text Link Idle", group = ANDES_COLOR_GROUP_TEXT)
internal val AndesDefaultTextLinkIdle = Color(0xFF434CE4)

@ShowkaseColor(name = "Andes Text Link Active", group = ANDES_COLOR_GROUP_TEXT)
internal val AndesDefaultTextLinkActive = Color(0xFF272C96)

// Brand Colors
@ShowkaseColor(name = "Andes Brand Fill Loud", group = ANDES_COLOR_GROUP_BRAND)
internal val AndesDefaultBrandFillLoud = Color(0xFFFFE600)

@ShowkaseColor(name = "Andes Brand Fill Quiet", group = ANDES_COLOR_GROUP_BRAND)
internal val AndesDefaultBrandFillQuiet = Color(0xFFFFF394)

@ShowkaseColor(name = "Andes Brand Gradient Start", group = ANDES_COLOR_GROUP_BRAND)
internal val AndesDefaultBrandGradientStart = Color(0xFFF9C200)

@ShowkaseColor(name = "Andes Brand Gradient End", group = ANDES_COLOR_GROUP_BRAND)
internal val AndesDefaultBrandGradientEnd = Color(0xFFFFE600)

// Feedback Informative Colors
@ShowkaseColor(name = "Andes Feedback Informative Fill Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackInformativeFillLoud = Color(0xFF434CE4)

@ShowkaseColor(name = "Andes Feedback Informative Fill Quiet", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackInformativeFillQuiet = Color(0xFFE9F1FF)

@ShowkaseColor(name = "Andes Feedback Informative Text Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackInformativeTextLoud = Color(0xFF434CE4)

@ShowkaseColor(name = "Andes Feedback Informative Border Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackInformativeBorderLoud = Color(0xFF5C70FA)

@ShowkaseColor(name = "Andes Feedback Informative Icon Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackInformativeIconLoud = Color(0xFF434CE4)

// Feedback Positive Colors
@ShowkaseColor(name = "Andes Feedback Positive Fill Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackPositiveFillLoud = Color(0xFF1F8923)

@ShowkaseColor(name = "Andes Feedback Positive Fill Quiet", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackPositiveFillQuiet = Color(0xFFDEFADE)

@ShowkaseColor(name = "Andes Feedback Positive Text Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackPositiveTextLoud = Color(0xFF1F8923)

@ShowkaseColor(name = "Andes Feedback Positive Border Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackPositiveBorderLoud = Color(0xFF14A919)

@ShowkaseColor(name = "Andes Feedback Positive Icon Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackPositiveIconLoud = Color(0xFF1F8923)

// Feedback Caution Colors
@ShowkaseColor(name = "Andes Feedback Caution Fill Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackCautionFillLoud = Color(0xFFD74009)

@ShowkaseColor(name = "Andes Feedback Caution Fill Quiet", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackCautionFillQuiet = Color(0xFFFFEDC7)

@ShowkaseColor(name = "Andes Feedback Caution Text Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackCautionTextLoud = Color(0xFFD74009)

@ShowkaseColor(name = "Andes Feedback Caution Border Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackCautionBorderLoud = Color(0xFFF05705)

@ShowkaseColor(name = "Andes Feedback Caution Icon Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackCautionIconLoud = Color(0xFFD74009)

// Feedback Negative Colors
@ShowkaseColor(name = "Andes Feedback Negative Fill Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackNegativeFillLoud = Color(0xFFC4031D)

@ShowkaseColor(name = "Andes Feedback Negative Fill Quiet", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackNegativeFillQuiet = Color(0xFFFFE5E9)

@ShowkaseColor(name = "Andes Feedback Negative Text Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackNegativeTextLoud = Color(0xFFC4031D)

@ShowkaseColor(name = "Andes Feedback Negative Border Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackNegativeBorderLoud = Color(0xFFED314A)

@ShowkaseColor(name = "Andes Feedback Negative Icon Loud", group = ANDES_COLOR_GROUP_FEEDBACK)
internal val AndesDefaultFeedbackNegativeIconLoud = Color(0xFFC4031D)

// Interactive Fill Loud Colors
@ShowkaseColor(name = "Andes Interactive Fill Loud Idle", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveFillLoudIdle = Color(0xFF434CE4)

@ShowkaseColor(name = "Andes Interactive Fill Loud Hover", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveFillLoudHover = Color(0xFF353AC5)

@ShowkaseColor(name = "Andes Interactive Fill Loud Active", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveFillLoudActive = Color(0xFF272C96)

// Interactive Fill Quiet Colors
@ShowkaseColor(name = "Andes Interactive Fill Quiet Idle", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveFillQuietIdle = Color(0xFFE9F1FF)

@ShowkaseColor(name = "Andes Interactive Fill Quiet Hover", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveFillQuietHover = Color(0xFFDEE9FF)

@ShowkaseColor(name = "Andes Interactive Fill Quiet Active", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveFillQuietActive = Color(0xFFC6D8FF)

// Interactive Fill Mute Colors
@ShowkaseColor(name = "Andes Interactive Fill Mute Idle", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveFillMuteIdle = Color(0x00FFFFFF)

@ShowkaseColor(name = "Andes Interactive Fill Mute Hover", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveFillMuteHover = Color(0xFFE9F1FF)

@ShowkaseColor(name = "Andes Interactive Fill Mute Active", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveFillMuteActive = Color(0xFFDEE9FF)

// Interactive Border Colors
@ShowkaseColor(name = "Andes Interactive Border Idle", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveBorderIdle = Color(0xFF8788AB)

@ShowkaseColor(name = "Andes Interactive Border Active", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveBorderActive = Color(0xFF434CE4)

// Interactive Icon Colors
@ShowkaseColor(name = "Andes Interactive Icon Idle", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveIconIdle = Color(0xFF646587)

@ShowkaseColor(name = "Andes Interactive Icon Active", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveIconActive = Color(0xFF282834)

@ShowkaseColor(name = "Andes Interactive Icon Idle Accent", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveIconIdleAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "Andes Interactive Icon Active Accent", group = ANDES_COLOR_GROUP_INTERACTIVE)
internal val AndesDefaultInteractiveIconActiveAccent = Color(0xFF272C96)

// Transparent Color
internal val AndesDefaultTransparent = Color(0x00FFFFFF)

// Andes Default Colors Configuration
internal val AndesDefaultLightColors = MercadoPagoAndesColor(
    background = MercadoPagoAndesBackgroundColor(
        primary = AndesDefaultBackgroundPrimary,
        secondary = AndesDefaultBackgroundSecondary,
    ),
    surface = MercadoPagoAndesSurfaceColor(
        primaryIdle = AndesDefaultSurfacePrimaryIdle,
        primaryActive = AndesDefaultSurfacePrimaryActive,
        primaryDisabled = AndesDefaultSurfacePrimaryDisabled,
    ),
    fill = MercadoPagoAndesFillColor(
        primary = AndesDefaultFillPrimary,
        secondary = AndesDefaultFillSecondary,
        inverse = AndesDefaultFillInverse,
        disabled = AndesDefaultFillDisabled,
        accentLoud = AndesDefaultFillAccentLoud,
        accentQuiet = AndesDefaultFillAccentQuiet,
        defaultOnScroll = AndesDefaultFillDefaultOnScroll,
    ),
    border = MercadoPagoAndesBorderColor(
        primary = AndesDefaultBorderPrimary,
        accent = AndesDefaultBorderAccent,
        inverse = AndesDefaultBorderInverse,
        disabled = AndesDefaultBorderDisabled,
    ),
    icon = MercadoPagoAndesIconColor(
        primary = AndesDefaultIconPrimary,
        secondary = AndesDefaultIconSecondary,
        accent = AndesDefaultIconAccent,
        inverse = AndesDefaultIconInverse,
        disabled = AndesDefaultIconDisabled,
    ),
    text = MercadoPagoAndesTextColor(
        primary = AndesDefaultTextPrimary,
        secondary = AndesDefaultTextSecondary,
        accent = AndesDefaultTextAccent,
        inverse = AndesDefaultTextInverse,
        disabled = AndesDefaultTextDisabled,
        linkIdle = AndesDefaultTextLinkIdle,
        linkActive = AndesDefaultTextLinkActive,
    ),
    brand = MercadoPagoAndesBrandColor(
        fillLoud = AndesDefaultBrandFillLoud,
        fillQuiet = AndesDefaultBrandFillQuiet,
        gradientStart = AndesDefaultBrandGradientStart,
        gradientEnd = AndesDefaultBrandGradientEnd,
    ),
    feedback = MercadoPagoAndesFeedbackColor(
        informative = MercadoPagoAndesFeedbackTypeColor(
            fillLoud = AndesDefaultFeedbackInformativeFillLoud,
            fillQuiet = AndesDefaultFeedbackInformativeFillQuiet,
            textLoud = AndesDefaultFeedbackInformativeTextLoud,
            borderLoud = AndesDefaultFeedbackInformativeBorderLoud,
            iconLoud = AndesDefaultFeedbackInformativeIconLoud,
        ),
        positive = MercadoPagoAndesFeedbackTypeColor(
            fillLoud = AndesDefaultFeedbackPositiveFillLoud,
            fillQuiet = AndesDefaultFeedbackPositiveFillQuiet,
            textLoud = AndesDefaultFeedbackPositiveTextLoud,
            borderLoud = AndesDefaultFeedbackPositiveBorderLoud,
            iconLoud = AndesDefaultFeedbackPositiveIconLoud,
        ),
        caution = MercadoPagoAndesFeedbackTypeColor(
            fillLoud = AndesDefaultFeedbackCautionFillLoud,
            fillQuiet = AndesDefaultFeedbackCautionFillQuiet,
            textLoud = AndesDefaultFeedbackCautionTextLoud,
            borderLoud = AndesDefaultFeedbackCautionBorderLoud,
            iconLoud = AndesDefaultFeedbackCautionIconLoud,
        ),
        negative = MercadoPagoAndesFeedbackTypeColor(
            fillLoud = AndesDefaultFeedbackNegativeFillLoud,
            fillQuiet = AndesDefaultFeedbackNegativeFillQuiet,
            textLoud = AndesDefaultFeedbackNegativeTextLoud,
            borderLoud = AndesDefaultFeedbackNegativeBorderLoud,
            iconLoud = AndesDefaultFeedbackNegativeIconLoud,
        ),
    ),
    interactive = MercadoPagoAndesInteractiveColor(
        fillLoud = MercadoPagoAndesInteractiveFillColor(
            idle = AndesDefaultInteractiveFillLoudIdle,
            hover = AndesDefaultInteractiveFillLoudHover,
            active = AndesDefaultInteractiveFillLoudActive,
        ),
        fillQuiet = MercadoPagoAndesInteractiveFillColor(
            idle = AndesDefaultInteractiveFillQuietIdle,
            hover = AndesDefaultInteractiveFillQuietHover,
            active = AndesDefaultInteractiveFillQuietActive,
        ),
        fillMute = MercadoPagoAndesInteractiveFillColor(
            idle = AndesDefaultInteractiveFillMuteIdle,
            hover = AndesDefaultInteractiveFillMuteHover,
            active = AndesDefaultInteractiveFillMuteActive,
        ),
        border = MercadoPagoAndesInteractiveBorderColor(
            idle = AndesDefaultInteractiveBorderIdle,
            active = AndesDefaultInteractiveBorderActive,
        ),
        icon = MercadoPagoAndesInteractiveIconColor(
            idle = AndesDefaultInteractiveIconIdle,
            active = AndesDefaultInteractiveIconActive,
            idleAccent = AndesDefaultInteractiveIconIdleAccent,
            activeAccent = AndesDefaultInteractiveIconActiveAccent,
        ),
    ),
    transparent = MercadoPagoAndesTransparentColor(
        transparent = AndesDefaultTransparent,
    ),
)

// New Default Spacing Configuration
internal val NewDefaultLightSpacingPaddings = AndesSpacingPaddings(
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

internal val NewDefaultLightSpacingGap = AndesSpacingGap(
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

internal val AndesDefaultLightSpacing = MercadoPagoAndesSpacing(
    paddings = NewDefaultLightSpacingPaddings,
    gap = NewDefaultLightSpacingGap,
)

// New Default Radius Configuration
internal val AndesDefaultLightRadius = MercadoPagoAndesRadius(
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
internal val AndesDefaultLightShape = MercadoPagoAndesShape(
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
internal val AndesDefaultLightBorderWidth = MercadoPagoAndesBorderWidth(
    none = 0.dp,
    small = 1.dp,
    medium = 2.dp,
    large = 3.dp,
    xlarge = 4.dp,
)

// New Default Typography Configuration
internal val InterFontFamily = FontFamily.Default

internal val MercadoPagoAndesDefaultLightTheme = MercadoPagoThemeProvider.Andes(
    color = AndesDefaultLightColors,
    spacing = AndesDefaultLightSpacing,
    shape = AndesDefaultLightShape,
    radius = AndesDefaultLightRadius,
    borderWidth = AndesDefaultLightBorderWidth,
    typography = AndesDefaultTypography,
)
