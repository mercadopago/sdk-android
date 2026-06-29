package com.mercadopago.sdk.android.data.local.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import app.cash.turbine.test
import com.mercadopago.sdk.android.domain.model.SiteId
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals

internal class SdkInitializationLocalDataSourceTest {

    private val dataStore = mockk<DataStore<Preferences>>(relaxed = true)
    private val messageDigest = mockk<MessageDigest>(relaxed = true)
    private val dataSource = SdkInitializationLocalDataSourceImpl(
        dataStore = dataStore,
        messageDigest = messageDigest,
    )

    @Test
    fun `when getSiteId is called Then return siteId`() = runTest {
        // Given
        val siteId = SiteId("123")
        val publicKey = "public_key"
        val encryptedPublicKeyBytes = "encrypted_public_key".toByteArray()
        val encryptedPublicKey = encryptedPublicKeyBytes.joinToString("") { FORMAT_MODE.format(it) }
        every {
            messageDigest.digest(publicKey.toByteArray())
        } returns encryptedPublicKeyBytes
        every { dataStore.data } returns flowOf(
            preferencesOf(
                stringPreferencesKey(encryptedPublicKey) to siteId.siteId
            )
        )

        // When
        val result = dataSource.getSiteId(publicKey)

        // Then
        result.test {
            assertEquals(siteId, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when setSiteId is called Then set siteId`() = runTest {
        // Given
        val siteId = SiteId("123")
        val publicKey = "public_key"
        val encryptedPublicKeyBytes = "encrypted_public_key".toByteArray()
        val encryptedPublicKey = encryptedPublicKeyBytes.toString()
        every {
            messageDigest.digest(publicKey.toByteArray())
        } returns encryptedPublicKeyBytes
        val preferences = preferencesOf(
            stringPreferencesKey(encryptedPublicKey) to siteId.siteId
        )
        coEvery {
            dataStore.updateData { (any()) }
        } returns preferences

        // When
        val result = dataSource.setSiteId(publicKey, siteId)

        // Then
        result.test {
            assertEquals(Unit, awaitItem())
            awaitComplete()
            assertEquals(siteId.siteId, preferences[stringPreferencesKey(encryptedPublicKey)])
        }
    }
}
