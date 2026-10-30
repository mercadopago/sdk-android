package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenSubtitleSegmentResponse
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSegmentState
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSubtitleSegment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class StatusScreenSubtitleSegmentsMapperTest {
    private fun paymentItemWith(
        subtitle: String? = null,
        segments: List<StatusScreenSubtitleSegmentResponse>?,
    ): StatusScreenBodyItem.ListItem {
        val node = paymentListItem()
        val withSegments = node.copy(data = node.data?.copy(subtitle = subtitle, subtitleSegments = segments))
        val response = approvedResponse(body = listOf(sellerListItem(), withSegments))
        val body = assertNotNull(response.toViewDataOrNull()).body
        return assertIs<StatusScreenBodyItem.ListItem>(body[1])
    }

    @Test
    fun `given subtitle segments then they are mapped in order with the success state`() {
        // Given
        val segments = listOf(
            StatusScreenSubtitleSegmentResponse(text = "\$ 1.000 (1x \$ 1.000 "),
            StatusScreenSubtitleSegmentResponse(text = "sin interés", state = "success"),
            StatusScreenSubtitleSegmentResponse(text = ")"),
        )

        // When
        val item = paymentItemWith(segments = segments)

        // Then
        assertEquals(
            listOf(
                StatusScreenSubtitleSegment(text = "\$ 1.000 (1x \$ 1.000 ", state = null),
                StatusScreenSubtitleSegment(text = "sin interés", state = StatusScreenSegmentState.Success),
                StatusScreenSubtitleSegment(text = ")", state = null),
            ),
            item.subtitleSegments,
        )
    }

    @Test
    fun `given segments and no subtitle then the subtitle is the joined segments text`() {
        // Given
        val segments = listOf(
            StatusScreenSubtitleSegmentResponse(text = "\$ 1.000 (1x \$ 1.000 "),
            StatusScreenSubtitleSegmentResponse(text = "sin interés", state = "success"),
            StatusScreenSubtitleSegmentResponse(text = ")"),
        )

        // When
        val item = paymentItemWith(subtitle = null, segments = segments)

        // Then
        assertEquals("\$ 1.000 (1x \$ 1.000 sin interés)", item.subtitle)
    }

    @Test
    fun `given segments and a subtitle then the subtitle is kept as is`() {
        // Given
        val segments = listOf(StatusScreenSubtitleSegmentResponse(text = "other"))

        // When
        val item = paymentItemWith(subtitle = "\$ 900 sin interés", segments = segments)

        // Then
        assertEquals("\$ 900 sin interés", item.subtitle)
    }

    @Test
    fun `given an unknown segment state then the segment has no state`() {
        // Given
        val segments = listOf(StatusScreenSubtitleSegmentResponse(text = "x", state = "something_new"))

        // When
        val item = paymentItemWith(segments = segments)

        // Then
        assertNull(item.subtitleSegments.single().state)
    }

    @Test
    fun `given a segment without text then it is dropped`() {
        // Given
        val segments = listOf(
            StatusScreenSubtitleSegmentResponse(text = null, state = "success"),
            StatusScreenSubtitleSegmentResponse(text = "ok"),
        )

        // When
        val item = paymentItemWith(segments = segments)

        // Then
        assertEquals(listOf("ok"), item.subtitleSegments.map { it.text })
    }

    @Test
    fun `given no segments then the list item keeps its plain subtitle and has no segments`() {
        // Given
        val subtitle = "\$ 900 sin interés"

        // When
        val item = paymentItemWith(subtitle = subtitle, segments = null)

        // Then
        assertEquals(subtitle, item.subtitle)
        assertEquals(emptyList(), item.subtitleSegments)
    }
}
