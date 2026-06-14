package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickScreenState
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class PaymentBrickViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        mockkObject(CheckoutCallbackHolder)
    }

    @After
    fun tearDown() {
        unmockkObject(CheckoutCallbackHolder)
    }

    @Test
    fun `when initialized, viewState is empty`() {
        val viewModel = PaymentBrickViewModel()

        assertEquals(PaymentBrickScreenState(), viewModel.viewState.value)
    }

    @Test
    fun `when onBackPressed, CheckoutCallbackHolder is not notified until context is defined`() {
        val viewModel = PaymentBrickViewModel()

        viewModel.onBackPressed()

        verify(exactly = 0) { CheckoutCallbackHolder.notify(any()) }
    }
}
