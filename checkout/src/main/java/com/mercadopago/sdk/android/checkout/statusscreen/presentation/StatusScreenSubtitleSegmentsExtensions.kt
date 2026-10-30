package com.mercadopago.sdk.android.checkout.statusscreen.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSegmentState
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSubtitleSegment

internal fun List<StatusScreenSubtitleSegment>.toAnnotatedString(
    colorFor: (StatusScreenSegmentState) -> Color?,
): AnnotatedString =
    buildAnnotatedString {
        forEach { segment ->
            val color = segment.state?.let(colorFor)
            if (color != null) {
                withStyle(SpanStyle(color = color)) { append(segment.text) }
            } else {
                append(segment.text)
            }
        }
    }
