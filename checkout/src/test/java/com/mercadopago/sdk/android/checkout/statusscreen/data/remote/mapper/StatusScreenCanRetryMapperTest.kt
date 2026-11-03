package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class StatusScreenCanRetryMapperTest {
    @Test
    fun `given an approved response then view data canRetry is absent`() {
        // When
        val viewData = assertNotNull(approvedResponse().toViewDataOrNull())

        // Then
        assertNull(viewData.canRetry)
    }

    @Test
    fun `given a pending response then view data canRetry is absent`() {
        // When
        val viewData = assertNotNull(approvedResponse(statusType = "pending").toViewDataOrNull())

        // Then
        assertNull(viewData.canRetry)
    }

    @Test
    fun `given a rejected response with can_retry true then view data canRetry is true`() {
        // Given
        val response = rejectedResponse(canRetry = true)

        // When
        val viewData = assertNotNull(response.toViewDataOrNull())

        // Then
        assertEquals(true, viewData.canRetry)
    }

    @Test
    fun `given a rejected response with can_retry false then view data canRetry is false`() {
        // Given
        val response = rejectedResponse(canRetry = false, buttons = listOf(backButton()))

        // When
        val viewData = assertNotNull(response.toViewDataOrNull())

        // Then
        assertEquals(false, viewData.canRetry)
    }

    @Test
    fun `given a rejected response without can_retry then view data canRetry is absent`() {
        // Given
        val response = rejectedResponse(canRetry = null, buttons = listOf(backButton()))

        // When
        val viewData = assertNotNull(response.toViewDataOrNull())

        // Then
        assertNull(viewData.canRetry)
    }
}
