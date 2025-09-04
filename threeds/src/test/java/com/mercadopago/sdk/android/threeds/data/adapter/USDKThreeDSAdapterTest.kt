package com.mercadopago.sdk.android.threeds.data.adapter

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mercadopago.sdk.android.threeds.domain.callback.MPThreeDSChallengeDelegate
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSDirectoryServer
import com.mercadopago.sdk.android.threeds.mockThreeDSAuthenticationModel
import com.usdk.android.UsdkThreeDS2ServiceImpl
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.emvco.threeds.core.AuthenticationRequestParameters
import org.emvco.threeds.core.ChallengeParameters
import org.emvco.threeds.core.ChallengeStatusReceiver
import org.emvco.threeds.core.CompletionEvent
import org.emvco.threeds.core.ConfigParameters
import org.emvco.threeds.core.ProtocolErrorEvent
import org.emvco.threeds.core.RuntimeErrorEvent
import org.emvco.threeds.core.ThreeDS2Service
import org.emvco.threeds.core.Transaction
import org.emvco.threeds.core.exceptions.InvalidInputException
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class USDKThreeDSAdapterTest {

    private val context = mockk<Context>(relaxed = true)
    private val activity = mockk<Activity>(relaxed = true)
    private val delegate = mockk<MPThreeDSChallengeDelegate>(relaxed = true)
    private val threeDSService = mockk<ThreeDS2Service>(relaxed = true)
    private val transaction = mockk<Transaction>(relaxed = true)
    private val localBroadcastManager = mockk<LocalBroadcastManager>(relaxed = true)

    private lateinit var adapter: USDKThreeDSAdapter

    @Before
    fun setup() {
        mockkConstructor(UsdkThreeDS2ServiceImpl::class)
        mockkStatic(LocalBroadcastManager::class)

        every { LocalBroadcastManager.getInstance(any()) } returns localBroadcastManager
        every { localBroadcastManager.registerReceiver(any(), any()) } just runs
        every { localBroadcastManager.unregisterReceiver(any()) } just runs

        adapter = USDKThreeDSAdapter(context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when initialize is successful Then should set threeDSService`() = runTest {
        // Given
        val intentSlot = slot<Intent>()
        every { anyConstructed<UsdkThreeDS2ServiceImpl>().initialize(any(), any(), any(), any()) } just runs

        // When
        adapter.initialize()

        // Simulate successful initialization broadcast
        val receiverSlot = slot<BroadcastReceiver>()
        verify { localBroadcastManager.registerReceiver(capture(receiverSlot), any()) }

        val successIntent = mockk<Intent> {
            every { getBooleanExtra(UsdkThreeDS2ServiceImpl.INITIALIZATION_ACTION_EXTRA_SUCCESS, false) } returns true
        }

        receiverSlot.captured.onReceive(context, successIntent)

        // Then
        verify { anyConstructed<UsdkThreeDS2ServiceImpl>().initialize(context, any<ConfigParameters>(), null, null) }
        verify { localBroadcastManager.unregisterReceiver(any()) }
    }

    @Test
    fun `when initialize fails Then should handle error gracefully`() = runTest {
        // Given
        every { anyConstructed<UsdkThreeDS2ServiceImpl>().initialize(any(), any(), any(), any()) } just runs

        // When
        adapter.initialize()

        // Simulate failed initialization broadcast
        val receiverSlot = slot<BroadcastReceiver>()
        verify { localBroadcastManager.registerReceiver(capture(receiverSlot), any()) }

        val failedIntent = mockk<Intent> {
            every { getBooleanExtra(UsdkThreeDS2ServiceImpl.INITIALIZATION_ACTION_EXTRA_SUCCESS, false) } returns false
            every { getStringExtra(UsdkThreeDS2ServiceImpl.INITIALIZATION_ACTION_EXTRA_ERROR_CODE) } returns "ERROR_CODE"
            every { getStringExtra(UsdkThreeDS2ServiceImpl.INITIALIZATION_ACTION_EXTRA_ERROR_TYPE) } returns "ERROR_TYPE"
        }

        receiverSlot.captured.onReceive(context, failedIntent)

        // Then
        verify { localBroadcastManager.unregisterReceiver(any()) }
    }

    @Test
    fun `when createTransaction is called Then should create transaction with directory server`() = runTest {
        // Given
        val directoryServer = MPThreeDSDirectoryServer.VISA
        setupInitializedAdapter()
        every { threeDSService.createTransaction(any(), any()) } returns transaction

        // When
        adapter.createTransaction(directoryServer)

        // Then
        verify { threeDSService.createTransaction(directoryServer.directoryServerID, directoryServer.messageVersion) }
    }

    @Test
    fun `when getAuthenticationRequestParameters is called with valid transaction Then should return parameters`() = runTest {
        // Given
        setupInitializedAdapterWithTransaction()
        val mockAuthParams = mockk<AuthenticationRequestParameters> {
            every { sdkAppID } returns "test_app_id"
            every { deviceData } returns "test_device_data"
            every { sdkEphemeralPublicKey } returns "test_public_key"
            every { sdkReferenceNumber } returns "test_ref_number"
            every { sdkTransactionID } returns "test_transaction_id"
        }
        every { transaction.authenticationRequestParameters } returns mockAuthParams

        // When
        val result = adapter.getAuthenticationRequestParameters()

        // Then
        assertNotNull(result)
        assertEquals("test_app_id", result.sdkAppId)
        assertEquals("test_device_data", result.deviceData)
        assertEquals("test_public_key", result.sdkEphemeralPublicKey)
        assertEquals("test_ref_number", result.sdkReferenceNumber)
        assertEquals("test_transaction_id", result.sdkTransactionId)
    }

    @Test
    fun `when getAuthenticationRequestParameters is called without transaction Then should return null`() = runTest {
        // Given
        setupInitializedAdapter()

        // When
        val result = adapter.getAuthenticationRequestParameters()

        // Then
        assertNull(result)
    }

    @Test
    fun `when doChallenge succeeds Then should call onSuccess`() = runTest {
        // Given
        setupInitializedAdapterWithTransaction()
        val authenticationResponse = mockThreeDSAuthenticationModel()
        val timeout = 5000
        val challengeReceiver = slot<ChallengeStatusReceiver>()

        every { transaction.doChallenge(any(), any(), capture(challengeReceiver), any()) } just runs

        // When
        adapter.doChallenge(activity, authenticationResponse, delegate, timeout)

        // Then
        verify { transaction.doChallenge(eq(activity), any<ChallengeParameters>(), any(), eq(timeout)) }

        // Simulate completion event
        val completionEvent = mockk<CompletionEvent> {
            every { transactionStatus } returns "TRUE"
        }
        challengeReceiver.captured.completed(completionEvent)

        verify { delegate.onSuccess(any()) }
    }

    @Test
    fun `when doChallenge has protocol error Then should call onError`() = runTest {
        // Given
        setupInitializedAdapterWithTransaction()
        val authenticationResponse = mockThreeDSAuthenticationModel()
        val timeout = 5000
        val challengeReceiver = slot<ChallengeStatusReceiver>()

        every { transaction.doChallenge(any(), any(), capture(challengeReceiver), any()) } just runs

        // When
        adapter.doChallenge(activity, authenticationResponse, delegate, timeout)

        // Then
        val protocolErrorEvent = mockk<ProtocolErrorEvent> {
            every { errorMessage.errorCode } returns "ERROR_CODE"
            every { errorMessage.errorDescription } returns "Error description"
            every { errorMessage.errorDetails } returns "Error details"
        }
        challengeReceiver.captured.protocolError(protocolErrorEvent)

        verify { delegate.onError(any()) }
    }

    @Test
    fun `when doChallenge has runtime error Then should call onError`() = runTest {
        // Given
        setupInitializedAdapterWithTransaction()
        val authenticationResponse = mockThreeDSAuthenticationModel()
        val timeout = 5000
        val challengeReceiver = slot<ChallengeStatusReceiver>()

        every { transaction.doChallenge(any(), any(), capture(challengeReceiver), any()) } just runs

        // When
        adapter.doChallenge(activity, authenticationResponse, delegate, timeout)

        // Then
        val runtimeErrorEvent = mockk<RuntimeErrorEvent> {
            every { errorCode } returns "RUNTIME_ERROR"
            every { errorMessage } returns "Runtime error message"
        }
        challengeReceiver.captured.runtimeError(runtimeErrorEvent)

        verify { delegate.onError(any()) }
    }

    @Test
    fun `when doChallenge is cancelled Then should call onCancel`() = runTest {
        // Given
        setupInitializedAdapterWithTransaction()
        val authenticationResponse = mockThreeDSAuthenticationModel()
        val timeout = 5000
        val challengeReceiver = slot<ChallengeStatusReceiver>()

        every { transaction.doChallenge(any(), any(), capture(challengeReceiver), any()) } just runs

        // When
        adapter.doChallenge(activity, authenticationResponse, delegate, timeout)

        // Then
        challengeReceiver.captured.cancelled()

        verify { delegate.onCancel() }
    }

    @Test
    fun `when doChallenge times out Then should call onTimedOut`() = runTest {
        // Given
        setupInitializedAdapterWithTransaction()
        val authenticationResponse = mockThreeDSAuthenticationModel()
        val timeout = 5000
        val challengeReceiver = slot<ChallengeStatusReceiver>()

        every { transaction.doChallenge(any(), any(), capture(challengeReceiver), any()) } just runs

        // When
        adapter.doChallenge(activity, authenticationResponse, delegate, timeout)

        // Then
        challengeReceiver.captured.timedout()

        verify { delegate.onTimedOut() }
    }

    @Test
    fun `when doChallenge throws InvalidInputException Then should handle gracefully`() = runTest {
        // Given
        setupInitializedAdapterWithTransaction()
        val authenticationResponse = mockThreeDSAuthenticationModel()
        val timeout = 5000

        every { transaction.doChallenge(any(), any(), any(), any()) } throws InvalidInputException("Invalid input")

        // When
        adapter.doChallenge(activity, authenticationResponse, delegate, timeout)

        // Then
        // Should not crash and handle the exception gracefully
        verify { transaction.doChallenge(any(), any(), any(), any()) }
    }

    private fun setupInitializedAdapter() {
        // Set up the adapter as if it's been initialized
        every { anyConstructed<UsdkThreeDS2ServiceImpl>().initialize(any(), any(), any(), any()) } just runs
        adapter = USDKThreeDSAdapter(context)

        // Access private field through reflection to set the service
        val field = USDKThreeDSAdapter::class.java.getDeclaredField("threeDSService")
        field.isAccessible = true
        field.set(adapter, threeDSService)
    }

    private fun setupInitializedAdapterWithTransaction() {
        setupInitializedAdapter()

        // Set up transaction
        val transactionField = USDKThreeDSAdapter::class.java.getDeclaredField("transaction")
        transactionField.isAccessible = true
        transactionField.set(adapter, transaction)
    }
}
