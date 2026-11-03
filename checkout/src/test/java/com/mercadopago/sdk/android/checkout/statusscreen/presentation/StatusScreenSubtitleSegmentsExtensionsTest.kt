package com.mercadopago.sdk.android.checkout.statusscreen.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSegmentState
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSubtitleSegment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class StatusScreenSubtitleSegmentsExtensionsTest {
    private val green = Color(0xFF1F8923)
    private val prefix = "\$ 1.000 (1x \$ 1.000 "
    private val interestFree = "sin interés"
    private val segments = listOf(
        StatusScreenSubtitleSegment(text = prefix),
        StatusScreenSubtitleSegment(text = interestFree, state = StatusScreenSegmentState.Success),
        StatusScreenSubtitleSegment(text = ")"),
    )

    private fun List<StatusScreenSubtitleSegment>.annotated(): AnnotatedString =
        toAnnotatedString { state ->
            when (state) {
                StatusScreenSegmentState.Success -> green
            }
        }

    @Test
    fun `given segments then the text is the joined segments in order`() {
        // Given
        val input = segments

        // When
        val annotated = input.annotated()

        // Then
        assertEquals("\$ 1.000 (1x \$ 1.000 sin interés)", annotated.text)
    }

    @Test
    fun `given a success segment then only its range gets the success color`() {
        // Given
        val input = segments

        // When
        val annotated = input.annotated()

        // Then
        val span = annotated.spanStyles.single()
        assertEquals(green, span.item.color)
        assertEquals(prefix.length, span.start)
        assertEquals(prefix.length + interestFree.length, span.end)
    }

    @Test
    fun `given segments without state then there are no spans`() {
        // Given
        val input = listOf(StatusScreenSubtitleSegment(text = "a"), StatusScreenSubtitleSegment(text = "b"))

        // When
        val annotated = input.annotated()

        // Then
        assertEquals("ab", annotated.text)
        assertTrue(annotated.spanStyles.isEmpty())
    }

    @Test
    fun `given a color rule that returns null then the segment is left unstyled`() {
        // Given
        val input = segments

        // When
        val annotated = input.toAnnotatedString { null }

        // Then
        assertTrue(annotated.spanStyles.isEmpty())
    }

    @Test
    fun `given no segments then the text is empty`() {
        // Given
        val input = emptyList<StatusScreenSubtitleSegment>()

        // When
        val annotated = input.annotated()

        // Then
        assertEquals("", annotated.text)
    }
}
