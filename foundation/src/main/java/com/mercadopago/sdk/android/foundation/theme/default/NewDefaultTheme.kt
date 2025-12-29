package com.mercadopago.sdk.android.foundation.theme.default

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseColor
import com.mercadopago.sdk.android.foundation.color.NewBackgroundColor
import com.mercadopago.sdk.android.foundation.color.NewBorderColor
import com.mercadopago.sdk.android.foundation.color.NewBrandColor
import com.mercadopago.sdk.android.foundation.color.NewFeedbackColor
import com.mercadopago.sdk.android.foundation.color.NewFeedbackTypeColor
import com.mercadopago.sdk.android.foundation.color.NewFillColor
import com.mercadopago.sdk.android.foundation.color.NewIconColor
import com.mercadopago.sdk.android.foundation.color.NewInteractiveBorderColor
import com.mercadopago.sdk.android.foundation.color.NewInteractiveColor
import com.mercadopago.sdk.android.foundation.color.NewInteractiveFillColor
import com.mercadopago.sdk.android.foundation.color.NewInteractiveIconColor
import com.mercadopago.sdk.android.foundation.color.NewMercadoPagoColor
import com.mercadopago.sdk.android.foundation.color.NewSurfaceColor
import com.mercadopago.sdk.android.foundation.color.NewTextColor
import com.mercadopago.sdk.android.foundation.color.NewTransparentColor
import com.mercadopago.sdk.android.foundation.outline.NewBorderWidth
import com.mercadopago.sdk.android.foundation.shape.NewMercadoPagoRadius
import com.mercadopago.sdk.android.foundation.shape.NewMercadoPagoShape
import com.mercadopago.sdk.android.foundation.spacing.NewMercadoPagoSpacing
import com.mercadopago.sdk.android.foundation.spacing.NewSpacingGap
import com.mercadopago.sdk.android.foundation.spacing.NewSpacingPaddings
import com.mercadopago.sdk.android.foundation.typography.NewHeadingTypography
import com.mercadopago.sdk.android.foundation.typography.NewMercadoPagoTypography
import com.mercadopago.sdk.android.foundation.typography.NewTypographyLetterSpacing
import com.mercadopago.sdk.android.foundation.typography.NewTypographyLineHeight
import com.mercadopago.sdk.android.foundation.typography.NewTypographyParagraphSpacing
import com.mercadopago.sdk.android.foundation.typography.NewTypographySize
import com.mercadopago.sdk.android.foundation.typography.NewTypographyWeight

private const val NEW_COLOR_GROUP_BACKGROUND = "New Background"
private const val NEW_COLOR_GROUP_SURFACE = "New Surface"
private const val NEW_COLOR_GROUP_FILL = "New Fill"
private const val NEW_COLOR_GROUP_BORDER = "New Border"
private const val NEW_COLOR_GROUP_ICON = "New Icon"
private const val NEW_COLOR_GROUP_TEXT = "New Text"
private const val NEW_COLOR_GROUP_BRAND = "New Brand"
private const val NEW_COLOR_GROUP_FEEDBACK = "New Feedback"
private const val NEW_COLOR_GROUP_INTERACTIVE = "New Interactive"

// Background Colors
@ShowkaseColor(name = "New Background Primary", group = NEW_COLOR_GROUP_BACKGROUND)
internal val NewDefaultBackgroundPrimary = Color(0xFFFFFFFF)

@ShowkaseColor(name = "New Background Secondary", group = NEW_COLOR_GROUP_BACKGROUND)
internal val NewDefaultBackgroundSecondary = Color(0xFFE7E9F3)

// Surface Colors
@ShowkaseColor(name = "New Surface Primary Idle", group = NEW_COLOR_GROUP_SURFACE)
internal val NewDefaultSurfacePrimaryIdle = Color(0xFFFFFFFF)

@ShowkaseColor(name = "New Surface Primary Active", group = NEW_COLOR_GROUP_SURFACE)
internal val NewDefaultSurfacePrimaryActive = Color(0xFFE7E9F3)

@ShowkaseColor(name = "New Surface Primary Disabled", group = NEW_COLOR_GROUP_SURFACE)
internal val NewDefaultSurfacePrimaryDisabled = Color(0x00FFFFFF)

// Fill Colors
@ShowkaseColor(name = "New Fill Primary", group = NEW_COLOR_GROUP_FILL)
internal val NewDefaultFillPrimary = Color(0xFFFFFFFF)

@ShowkaseColor(name = "New Fill Secondary", group = NEW_COLOR_GROUP_FILL)
internal val NewDefaultFillSecondary = Color(0xFFD0D4E6)

@ShowkaseColor(name = "New Fill Inverse", group = NEW_COLOR_GROUP_FILL)
internal val NewDefaultFillInverse = Color(0xFF282834)

@ShowkaseColor(name = "New Fill Disabled", group = NEW_COLOR_GROUP_FILL)
internal val NewDefaultFillDisabled = Color(0xFFD0D4E6)

@ShowkaseColor(name = "New Fill Accent Loud", group = NEW_COLOR_GROUP_FILL)
internal val NewDefaultFillAccentLoud = Color(0xFF434CE4)

@ShowkaseColor(name = "New Fill Accent Quiet", group = NEW_COLOR_GROUP_FILL)
internal val NewDefaultFillAccentQuiet = Color(0xFFE9F1FF)

@ShowkaseColor(name = "New Fill Default On Scroll", group = NEW_COLOR_GROUP_FILL)
internal val NewDefaultFillDefaultOnScroll = Color(0x99FFFFFF)

// Border Colors
@ShowkaseColor(name = "New Border Primary", group = NEW_COLOR_GROUP_BORDER)
internal val NewDefaultBorderPrimary = Color(0xFFD0D4E6)

@ShowkaseColor(name = "New Border Accent", group = NEW_COLOR_GROUP_BORDER)
internal val NewDefaultBorderAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "New Border Inverse", group = NEW_COLOR_GROUP_BORDER)
internal val NewDefaultBorderInverse = Color(0xFFFFFFFF)

@ShowkaseColor(name = "New Border Disabled", group = NEW_COLOR_GROUP_BORDER)
internal val NewDefaultBorderDisabled = Color(0xFFB5B9D4)

// Icon Colors
@ShowkaseColor(name = "New Icon Primary", group = NEW_COLOR_GROUP_ICON)
internal val NewDefaultIconPrimary = Color(0xFF282834)

@ShowkaseColor(name = "New Icon Secondary", group = NEW_COLOR_GROUP_ICON)
internal val NewDefaultIconSecondary = Color(0xFF646587)

@ShowkaseColor(name = "New Icon Accent", group = NEW_COLOR_GROUP_ICON)
internal val NewDefaultIconAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "New Icon Inverse", group = NEW_COLOR_GROUP_ICON)
internal val NewDefaultIconInverse = Color(0xFFFFFFFF)

@ShowkaseColor(name = "New Icon Disabled", group = NEW_COLOR_GROUP_ICON)
internal val NewDefaultIconDisabled = Color(0xFF9C9EBF)

// Text Colors
@ShowkaseColor(name = "New Text Primary", group = NEW_COLOR_GROUP_TEXT)
internal val NewDefaultTextPrimary = Color(0xFF282834)

@ShowkaseColor(name = "New Text Secondary", group = NEW_COLOR_GROUP_TEXT)
internal val NewDefaultTextSecondary = Color(0xFF646587)

@ShowkaseColor(name = "New Text Accent", group = NEW_COLOR_GROUP_TEXT)
internal val NewDefaultTextAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "New Text Inverse", group = NEW_COLOR_GROUP_TEXT)
internal val NewDefaultTextInverse = Color(0xFFFFFFFF)

@ShowkaseColor(name = "New Text Disabled", group = NEW_COLOR_GROUP_TEXT)
internal val NewDefaultTextDisabled = Color(0xFF9C9EBF)

@ShowkaseColor(name = "New Text Link Idle", group = NEW_COLOR_GROUP_TEXT)
internal val NewDefaultTextLinkIdle = Color(0xFF434CE4)

@ShowkaseColor(name = "New Text Link Active", group = NEW_COLOR_GROUP_TEXT)
internal val NewDefaultTextLinkActive = Color(0xFF272C96)

// Brand Colors
@ShowkaseColor(name = "New Brand Fill Loud", group = NEW_COLOR_GROUP_BRAND)
internal val NewDefaultBrandFillLoud = Color(0xFFFFE600)

@ShowkaseColor(name = "New Brand Fill Quiet", group = NEW_COLOR_GROUP_BRAND)
internal val NewDefaultBrandFillQuiet = Color(0xFFFFF394)

@ShowkaseColor(name = "New Brand Gradient Start", group = NEW_COLOR_GROUP_BRAND)
internal val NewDefaultBrandGradientStart = Color(0xFFF9C200)

@ShowkaseColor(name = "New Brand Gradient End", group = NEW_COLOR_GROUP_BRAND)
internal val NewDefaultBrandGradientEnd = Color(0xFFFFE600)

// Feedback Informative Colors
@ShowkaseColor(name = "New Feedback Informative Fill Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackInformativeFillLoud = Color(0xFF434CE4)

@ShowkaseColor(name = "New Feedback Informative Fill Quiet", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackInformativeFillQuiet = Color(0xFFE9F1FF)

@ShowkaseColor(name = "New Feedback Informative Text Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackInformativeTextLoud = Color(0xFF434CE4)

@ShowkaseColor(name = "New Feedback Informative Border Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackInformativeBorderLoud = Color(0xFF5C70FA)

@ShowkaseColor(name = "New Feedback Informative Icon Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackInformativeIconLoud = Color(0xFF434CE4)

// Feedback Positive Colors
@ShowkaseColor(name = "New Feedback Positive Fill Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackPositiveFillLoud = Color(0xFF1F8923)

@ShowkaseColor(name = "New Feedback Positive Fill Quiet", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackPositiveFillQuiet = Color(0xFFDEFADE)

@ShowkaseColor(name = "New Feedback Positive Text Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackPositiveTextLoud = Color(0xFF1F8923)

@ShowkaseColor(name = "New Feedback Positive Border Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackPositiveBorderLoud = Color(0xFF14A919)

@ShowkaseColor(name = "New Feedback Positive Icon Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackPositiveIconLoud = Color(0xFF1F8923)

// Feedback Caution Colors
@ShowkaseColor(name = "New Feedback Caution Fill Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackCautionFillLoud = Color(0xFFD74009)

@ShowkaseColor(name = "New Feedback Caution Fill Quiet", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackCautionFillQuiet = Color(0xFFFFEDC7)

@ShowkaseColor(name = "New Feedback Caution Text Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackCautionTextLoud = Color(0xFFD74009)

@ShowkaseColor(name = "New Feedback Caution Border Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackCautionBorderLoud = Color(0xFFF05705)

@ShowkaseColor(name = "New Feedback Caution Icon Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackCautionIconLoud = Color(0xFFD74009)

// Feedback Negative Colors
@ShowkaseColor(name = "New Feedback Negative Fill Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackNegativeFillLoud = Color(0xFFC4031D)

@ShowkaseColor(name = "New Feedback Negative Fill Quiet", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackNegativeFillQuiet = Color(0xFFFFE5E9)

@ShowkaseColor(name = "New Feedback Negative Text Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackNegativeTextLoud = Color(0xFFC4031D)

@ShowkaseColor(name = "New Feedback Negative Border Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackNegativeBorderLoud = Color(0xFFED314A)

@ShowkaseColor(name = "New Feedback Negative Icon Loud", group = NEW_COLOR_GROUP_FEEDBACK)
internal val NewDefaultFeedbackNegativeIconLoud = Color(0xFFC4031D)

// Interactive Fill Loud Colors
@ShowkaseColor(name = "New Interactive Fill Loud Idle", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveFillLoudIdle = Color(0xFF434CE4)

@ShowkaseColor(name = "New Interactive Fill Loud Hover", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveFillLoudHover = Color(0xFF353AC5)

@ShowkaseColor(name = "New Interactive Fill Loud Active", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveFillLoudActive = Color(0xFF272C96)

// Interactive Fill Quiet Colors
@ShowkaseColor(name = "New Interactive Fill Quiet Idle", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveFillQuietIdle = Color(0xFFE9F1FF)

@ShowkaseColor(name = "New Interactive Fill Quiet Hover", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveFillQuietHover = Color(0xFFDEE9FF)

@ShowkaseColor(name = "New Interactive Fill Quiet Active", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveFillQuietActive = Color(0xFFC6D8FF)

// Interactive Fill Mute Colors
@ShowkaseColor(name = "New Interactive Fill Mute Idle", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveFillMuteIdle = Color(0x00FFFFFF)

@ShowkaseColor(name = "New Interactive Fill Mute Hover", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveFillMuteHover = Color(0xFFE9F1FF)

@ShowkaseColor(name = "New Interactive Fill Mute Active", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveFillMuteActive = Color(0xFFDEE9FF)

// Interactive Border Colors
@ShowkaseColor(name = "New Interactive Border Idle", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveBorderIdle = Color(0xFF8788AB)

@ShowkaseColor(name = "New Interactive Border Active", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveBorderActive = Color(0xFF434CE4)

// Interactive Icon Colors
@ShowkaseColor(name = "New Interactive Icon Idle", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveIconIdle = Color(0xFF646587)

@ShowkaseColor(name = "New Interactive Icon Active", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveIconActive = Color(0xFF282834)

@ShowkaseColor(name = "New Interactive Icon Idle Accent", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveIconIdleAccent = Color(0xFF434CE4)

@ShowkaseColor(name = "New Interactive Icon Active Accent", group = NEW_COLOR_GROUP_INTERACTIVE)
internal val NewDefaultInteractiveIconActiveAccent = Color(0xFF272C96)

// Transparent Color
internal val NewDefaultTransparent = Color(0x00FFFFFF)

// New Default Colors Configuration
internal val NewDefaultLightColors = NewMercadoPagoColor(
    background = NewBackgroundColor(
        primary = NewDefaultBackgroundPrimary,
        secondary = NewDefaultBackgroundSecondary,
    ),
    surface = NewSurfaceColor(
        primaryIdle = NewDefaultSurfacePrimaryIdle,
        primaryActive = NewDefaultSurfacePrimaryActive,
        primaryDisabled = NewDefaultSurfacePrimaryDisabled,
    ),
    fill = NewFillColor(
        primary = NewDefaultFillPrimary,
        secondary = NewDefaultFillSecondary,
        inverse = NewDefaultFillInverse,
        disabled = NewDefaultFillDisabled,
        accentLoud = NewDefaultFillAccentLoud,
        accentQuiet = NewDefaultFillAccentQuiet,
        defaultOnScroll = NewDefaultFillDefaultOnScroll,
    ),
    border = NewBorderColor(
        primary = NewDefaultBorderPrimary,
        accent = NewDefaultBorderAccent,
        inverse = NewDefaultBorderInverse,
        disabled = NewDefaultBorderDisabled,
    ),
    icon = NewIconColor(
        primary = NewDefaultIconPrimary,
        secondary = NewDefaultIconSecondary,
        accent = NewDefaultIconAccent,
        inverse = NewDefaultIconInverse,
        disabled = NewDefaultIconDisabled,
    ),
    text = NewTextColor(
        primary = NewDefaultTextPrimary,
        secondary = NewDefaultTextSecondary,
        accent = NewDefaultTextAccent,
        inverse = NewDefaultTextInverse,
        disabled = NewDefaultTextDisabled,
        linkIdle = NewDefaultTextLinkIdle,
        linkActive = NewDefaultTextLinkActive,
    ),
    brand = NewBrandColor(
        fillLoud = NewDefaultBrandFillLoud,
        fillQuiet = NewDefaultBrandFillQuiet,
        gradientStart = NewDefaultBrandGradientStart,
        gradientEnd = NewDefaultBrandGradientEnd,
    ),
    feedback = NewFeedbackColor(
        informative = NewFeedbackTypeColor(
            fillLoud = NewDefaultFeedbackInformativeFillLoud,
            fillQuiet = NewDefaultFeedbackInformativeFillQuiet,
            textLoud = NewDefaultFeedbackInformativeTextLoud,
            borderLoud = NewDefaultFeedbackInformativeBorderLoud,
            iconLoud = NewDefaultFeedbackInformativeIconLoud,
        ),
        positive = NewFeedbackTypeColor(
            fillLoud = NewDefaultFeedbackPositiveFillLoud,
            fillQuiet = NewDefaultFeedbackPositiveFillQuiet,
            textLoud = NewDefaultFeedbackPositiveTextLoud,
            borderLoud = NewDefaultFeedbackPositiveBorderLoud,
            iconLoud = NewDefaultFeedbackPositiveIconLoud,
        ),
        caution = NewFeedbackTypeColor(
            fillLoud = NewDefaultFeedbackCautionFillLoud,
            fillQuiet = NewDefaultFeedbackCautionFillQuiet,
            textLoud = NewDefaultFeedbackCautionTextLoud,
            borderLoud = NewDefaultFeedbackCautionBorderLoud,
            iconLoud = NewDefaultFeedbackCautionIconLoud,
        ),
        negative = NewFeedbackTypeColor(
            fillLoud = NewDefaultFeedbackNegativeFillLoud,
            fillQuiet = NewDefaultFeedbackNegativeFillQuiet,
            textLoud = NewDefaultFeedbackNegativeTextLoud,
            borderLoud = NewDefaultFeedbackNegativeBorderLoud,
            iconLoud = NewDefaultFeedbackNegativeIconLoud,
        ),
    ),
    interactive = NewInteractiveColor(
        fillLoud = NewInteractiveFillColor(
            idle = NewDefaultInteractiveFillLoudIdle,
            hover = NewDefaultInteractiveFillLoudHover,
            active = NewDefaultInteractiveFillLoudActive,
        ),
        fillQuiet = NewInteractiveFillColor(
            idle = NewDefaultInteractiveFillQuietIdle,
            hover = NewDefaultInteractiveFillQuietHover,
            active = NewDefaultInteractiveFillQuietActive,
        ),
        fillMute = NewInteractiveFillColor(
            idle = NewDefaultInteractiveFillMuteIdle,
            hover = NewDefaultInteractiveFillMuteHover,
            active = NewDefaultInteractiveFillMuteActive,
        ),
        border = NewInteractiveBorderColor(
            idle = NewDefaultInteractiveBorderIdle,
            active = NewDefaultInteractiveBorderActive,
        ),
        icon = NewInteractiveIconColor(
            idle = NewDefaultInteractiveIconIdle,
            active = NewDefaultInteractiveIconActive,
            idleAccent = NewDefaultInteractiveIconIdleAccent,
            activeAccent = NewDefaultInteractiveIconActiveAccent,
        ),
    ),
    transparent = NewTransparentColor(
        transparent = NewDefaultTransparent,
    ),
)

// New Default Spacing Configuration
internal val NewDefaultLightSpacingPaddings = NewSpacingPaddings(
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

internal val NewDefaultLightSpacingGap = NewSpacingGap(
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

internal val NewDefaultLightSpacing = NewMercadoPagoSpacing(
    paddings = NewDefaultLightSpacingPaddings,
    gap = NewDefaultLightSpacingGap,
)

// New Default Radius Configuration
internal val NewDefaultLightRadius = NewMercadoPagoRadius(
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
internal val NewDefaultLightShape = NewMercadoPagoShape(
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
internal val NewDefaultLightBorderWidth = NewBorderWidth(
    none = 0.dp,
    small = 1.dp,
    medium = 2.dp,
    large = 3.dp,
    xlarge = 4.dp,
)

// New Default Typography Configuration
internal val InterFontFamily = FontFamily.Default

internal val NewDefaultLightTypography = NewMercadoPagoTypography(
    heading = NewHeadingTypography(
        familyDefault = InterFontFamily,
        size = NewTypographySize(
            size10 = 10.sp,
            size12 = 12.sp,
            size14 = 14.sp,
            size16 = 16.sp,
            size18 = 18.sp,
            size20 = 20.sp,
            size24 = 24.sp,
            size28 = 28.sp,
            size32 = 32.sp,
            size40 = 40.sp,
            size48 = 48.sp,
            size56 = 56.sp,
        ),
        lineHeight = NewTypographyLineHeight(
            lineHeight12 = 12.sp,
            lineHeight16 = 16.sp,
            lineHeight18 = 18.sp,
            lineHeight20 = 20.sp,
            lineHeight22 = 22.sp,
            lineHeight24 = 24.sp,
            lineHeight28 = 28.sp,
            lineHeight34 = 34.sp,
            lineHeight40 = 40.sp,
            lineHeight48 = 48.sp,
            lineHeight56 = 56.sp,
            lineHeight66 = 66.sp,
        ),
        weight = NewTypographyWeight(
            regular = FontWeight.W400,
            semibold = FontWeight.W600,
            bold = FontWeight.W700,
        ),
        letterSpacing = NewTypographyLetterSpacing(
            spacing0 = 0.sp,
            spacingNegative1 = (-1).sp,
        ),
        paragraphSpacing = NewTypographyParagraphSpacing(
            spacing10 = 10.sp,
            spacing12 = 12.sp,
            spacing14 = 14.sp,
            spacing16 = 16.sp,
        ),
    ),
)
