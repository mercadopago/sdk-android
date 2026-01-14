package com.mercadopago.sdk.android.foundation.typography

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Typography tokens for Andes design system.
 * These tokens map CSS design tokens to Android Compose values.
 */
internal object AndesTypographyTokens {
    /**
     * Font family token.
     * Maps to: --text-heading-family-default: inter
     */
    val FontFamilyDefault: FontFamily = FontFamily.Default

    /**
     * Font weight tokens.
     * Maps to:
     * - --text-heading-weight-regular: 400
     * - --text-heading-weight-semibold: 600
     * - --text-heading-weight-bold: 700
     */
    val FontWeightRegular: FontWeight = FontWeight.W400
    val FontWeightSemibold: FontWeight = FontWeight.W600
    val FontWeightBold: FontWeight = FontWeight.W700

    /**
     * Font size tokens for body text.
     * Maps to: --text-heading-size-* (body uses same size tokens as heading)
     * Based on design system specifications:
     * - small: 14sp (--text-heading-size-14)
     * - medium: 16sp (--text-heading-size-16)
     * - large: 18sp (--text-heading-size-18)
     */
    val BodySizeSmall: Float = 14f
    val BodySizeMedium: Float = 16f
    val BodySizeLarge: Float = 18f

    /**
     * Font size tokens for heading text.
     * Maps to: --text-heading-size-*
     * - small: 16sp (--text-heading-size-16)
     * - medium: 20sp (--text-heading-size-20)
     * - huge: 48sp (--text-heading-size-48)
     */
    val HeadingSizeSmall: Float = 16f
    val HeadingSizeMedium: Float = 20f
    val HeadingSizeHuge: Float = 48f

    /**
     * Line height tokens for body text.
     * Maps to: --text-heading-line-height-* (body uses same line-height tokens as heading)
     * Based on design system specifications:
     * - small: 20sp (--text-heading-line-height-20)
     * - medium: 24sp (--text-heading-line-height-24)
     * - large: 28sp (--text-heading-line-height-28)
     */
    val BodyLineHeightSmall: Float = 20f
    val BodyLineHeightMedium: Float = 24f
    val BodyLineHeightLarge: Float = 28f

    /**
     * Line height tokens for heading text.
     * Maps to: --text-heading-line-height-*
     * - small: 22sp (--text-heading-line-height-22)
     * - medium: 28sp (--text-heading-line-height-28)
     * - huge: 56sp (--text-heading-line-height-56)
     */
    val HeadingLineHeightSmall: Float = 22f
    val HeadingLineHeightMedium: Float = 28f
    val HeadingLineHeightHuge: Float = 56f

    /**
     * Letter spacing tokens.
     * Maps to:
     * - --text-heading-letter-spacing-0: 0
     * - --text-heading-letter-spacing--1: -1
     */
    val LetterSpacingDefault: Float = 0f
    val LetterSpacingNarrow: Float = -1f

    /**
     * Paragraph spacing tokens (for reference, not directly used in TextStyle).
     * Maps to: --text-heading-paragraph-spacing-*
     * These are used in layout spacing, not in TextStyle itself.
     */
    val ParagraphSpacingSmall: Float = 10f
    val ParagraphSpacingMedium: Float = 12f
    val ParagraphSpacingLarge: Float = 16f
}
