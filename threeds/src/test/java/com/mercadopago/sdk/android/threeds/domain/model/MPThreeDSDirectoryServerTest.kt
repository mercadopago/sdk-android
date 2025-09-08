package com.mercadopago.sdk.android.threeds.domain.model

import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSDirectoryServer
import org.junit.Test
import kotlin.test.assertEquals

internal class MPThreeDSDirectoryServerTest {

    @Test
    fun `when VISA directory server is accessed Then should have correct values`() {
        // Given
        val directoryServer = MPThreeDSDirectoryServer.VISA

        // Then
        assertEquals("A000000003", directoryServer.directoryServerID)
        assertEquals("2.1.0", directoryServer.messageVersion)
    }

    @Test
    fun `when MASTERCARD directory server is accessed Then should have correct values`() {
        // Given
        val directoryServer = MPThreeDSDirectoryServer.MASTERCARD

        // Then
        assertEquals("A000000004", directoryServer.directoryServerID)
        assertEquals("2.1.0", directoryServer.messageVersion)
    }

    @Test
    fun `when AMEX directory server is accessed Then should have correct values`() {
        // Given
        val directoryServer = MPThreeDSDirectoryServer.AMEX

        // Then
        assertEquals("A000000025", directoryServer.directoryServerID)
        assertEquals("2.1.0", directoryServer.messageVersion)
    }

    @Test
    fun `when paymentMethodDirectoryServer is called with visa Then should return VISA`() {
        // When
        val result = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("visa")

        // Then
        assertEquals(MPThreeDSDirectoryServer.VISA, result)
    }

    @Test
    fun `when paymentMethodDirectoryServer is called with debvisa Then should return VISA`() {
        // When
        val result = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("debvisa")

        // Then
        assertEquals(MPThreeDSDirectoryServer.VISA, result)
    }

    @Test
    fun `when paymentMethodDirectoryServer is called with mastercard Then should return MASTERCARD`() {
        // When
        val result = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("mastercard")

        // Then
        assertEquals(MPThreeDSDirectoryServer.MASTERCARD, result)
    }

    @Test
    fun `when paymentMethodDirectoryServer is called with master Then should return MASTERCARD`() {
        // When
        val result = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("master")

        // Then
        assertEquals(MPThreeDSDirectoryServer.MASTERCARD, result)
    }

    @Test
    fun `when paymentMethodDirectoryServer is called with amex Then should return AMEX`() {
        // When
        val result = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("amex")

        // Then
        assertEquals(MPThreeDSDirectoryServer.AMEX, result)
    }

    @Test
    fun `when paymentMethodDirectoryServer is called with american_express Then should return AMEX`() {
        // When
        val result = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("american_express")

        // Then
        assertEquals(MPThreeDSDirectoryServer.AMEX, result)
    }

    @Test
    fun `when paymentMethodDirectoryServer is called with unknown payment method Then should return MASTERCARD`() {
        // When
        val result = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("unknown_payment_method")

        // Then
        assertEquals(MPThreeDSDirectoryServer.MASTERCARD, result)
    }

    @Test
    fun `when paymentMethodDirectoryServer is called with empty string Then should return MASTERCARD`() {
        // When
        val result = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("")

        // Then
        assertEquals(MPThreeDSDirectoryServer.MASTERCARD, result)
    }

    @Test
    fun `when paymentMethodDirectoryServer is called with null Then should return MASTERCARD`() {
        // When
        val result = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("null")

        // Then
        assertEquals(MPThreeDSDirectoryServer.MASTERCARD, result)
    }
}
