package com.mercadopago.sdk.android.foundation.typography

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

/**
 * Represents the new typography system configuration for MercadoPago UI components.
 * This class defines the updated text style values used throughout the application.
 *
 * @property heading Heading typography configuration
 */
data class NewMercadoPagoTypography(
    val heading: NewHeadingTypography,
)

/**
 * Represents the heading typography configuration.
 *
 * @property familyDefault Default font family
 * @property size Text size configuration
 * @property lineHeight Line height configuration
 * @property weight Font weight configuration
 * @property letterSpacing Letter spacing configuration
 * @property paragraphSpacing Paragraph spacing configuration
 */
data class NewHeadingTypography(
    val familyDefault: FontFamily,
    val size: NewTypographySize,
    val lineHeight: NewTypographyLineHeight,
    val weight: NewTypographyWeight,
    val letterSpacing: NewTypographyLetterSpacing,
    val paragraphSpacing: NewTypographyParagraphSpacing,
)

/**
 * Represents the typography size configuration.
 *
 * @property size10 Size 10sp
 * @property size12 Size 12sp
 * @property size14 Size 14sp
 * @property size16 Size 16sp
 * @property size18 Size 18sp
 * @property size20 Size 20sp
 * @property size24 Size 24sp
 * @property size28 Size 28sp
 * @property size32 Size 32sp
 * @property size40 Size 40sp
 * @property size48 Size 48sp
 * @property size56 Size 56sp
 */
data class NewTypographySize(
    val size10: TextUnit,
    val size12: TextUnit,
    val size14: TextUnit,
    val size16: TextUnit,
    val size18: TextUnit,
    val size20: TextUnit,
    val size24: TextUnit,
    val size28: TextUnit,
    val size32: TextUnit,
    val size40: TextUnit,
    val size48: TextUnit,
    val size56: TextUnit,
)

/**
 * Represents the typography line height configuration.
 *
 * @property lineHeight12 Line height 12sp
 * @property lineHeight16 Line height 16sp
 * @property lineHeight18 Line height 18sp
 * @property lineHeight20 Line height 20sp
 * @property lineHeight22 Line height 22sp
 * @property lineHeight24 Line height 24sp
 * @property lineHeight28 Line height 28sp
 * @property lineHeight34 Line height 34sp
 * @property lineHeight40 Line height 40sp
 * @property lineHeight48 Line height 48sp
 * @property lineHeight56 Line height 56sp
 * @property lineHeight66 Line height 66sp
 */
data class NewTypographyLineHeight(
    val lineHeight12: TextUnit,
    val lineHeight16: TextUnit,
    val lineHeight18: TextUnit,
    val lineHeight20: TextUnit,
    val lineHeight22: TextUnit,
    val lineHeight24: TextUnit,
    val lineHeight28: TextUnit,
    val lineHeight34: TextUnit,
    val lineHeight40: TextUnit,
    val lineHeight48: TextUnit,
    val lineHeight56: TextUnit,
    val lineHeight66: TextUnit,
)

/**
 * Represents the typography weight configuration.
 *
 * @property regular Regular weight (400)
 * @property semibold Semibold weight (600)
 * @property bold Bold weight (700)
 */
data class NewTypographyWeight(
    val regular: FontWeight,
    val semibold: FontWeight,
    val bold: FontWeight,
)

/**
 * Represents the typography letter spacing configuration.
 *
 * @property spacing0 No letter spacing (0)
 * @property spacingNegative1 Negative letter spacing (-1)
 */
data class NewTypographyLetterSpacing(
    val spacing0: TextUnit,
    val spacingNegative1: TextUnit,
)

/**
 * Represents the typography paragraph spacing configuration.
 *
 * @property spacing10 Paragraph spacing 10
 * @property spacing12 Paragraph spacing 12
 * @property spacing14 Paragraph spacing 14
 * @property spacing16 Paragraph spacing 16
 */
data class NewTypographyParagraphSpacing(
    val spacing10: TextUnit,
    val spacing12: TextUnit,
    val spacing14: TextUnit,
    val spacing16: TextUnit,
)
