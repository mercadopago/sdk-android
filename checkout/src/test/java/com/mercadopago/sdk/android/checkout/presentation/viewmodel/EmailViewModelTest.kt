package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.presentation.state.EmailScreenState
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
internal class EmailViewModelTest {
    private lateinit var viewModel: EmailViewModel

    private val labels = EmailScreenState.Labels(
        title = "Title",
        fieldLabel = "Email",
        fieldPlaceholder = "placeholder@email.com",
        buttonLabel = "Continue",
        errorFieldEmpty = "Field is empty",
        errorEmailInvalid = "Invalid email",
        errorFieldRequired = "Field required",
    )

    @Before
    fun setup() {
        viewModel = EmailViewModel()
    }

    // region initialize

    @Test
    fun `when initialize with null baseEmail then state has empty email and button disabled`() {
        viewModel.initialize(labels, baseEmail = null)

        val state = viewModel.viewState.value!!
        assertEquals("", state.email)
        assertFalse(state.isButtonEnabled)
        assertFalse(state.isError)
        assertEquals("", state.errorMessage)
    }

    @Test
    fun `when initialize with valid baseEmail then button is enabled`() {
        viewModel.initialize(labels, baseEmail = "user@example.com")

        val state = viewModel.viewState.value!!
        assertEquals("user@example.com", state.email)
        assertTrue(state.isButtonEnabled)
        assertFalse(state.isError)
        assertEquals("", state.errorMessage)
    }

    @Test
    fun `when initialize with invalid baseEmail then button is disabled and no error shown`() {
        viewModel.initialize(labels, baseEmail = "not-an-email")

        val state = viewModel.viewState.value!!
        assertFalse(state.isButtonEnabled)
        assertFalse(state.isError)
        assertEquals("", state.errorMessage)
    }

    @Test
    fun `when not initialized then viewState is null`() {
        assertNull(viewModel.viewState.value)
    }

    // endregion

    // region onEmailChanged — isValidEmail

    @Test
    fun `when typed valid email then button is enabled and no error`() {
        viewModel.initialize(labels)

        viewModel.onEmailChanged("user@example.com")

        val state = viewModel.viewState.value!!
        assertTrue(state.isButtonEnabled)
        assertFalse(state.isError)
        assertEquals("", state.errorMessage)
    }

    @Test
    fun `when typed valid email with subdomain then button is enabled`() {
        viewModel.initialize(labels)

        viewModel.onEmailChanged("user@mail.example.com")

        assertTrue(viewModel.viewState.value!!.isButtonEnabled)
    }

    @Test
    fun `when typed valid email with plus sign then button is enabled`() {
        viewModel.initialize(labels)

        viewModel.onEmailChanged("user+tag@example.com")

        assertTrue(viewModel.viewState.value!!.isButtonEnabled)
    }

    // endregion

    // region onEmailChanged — isInvalidFormat

    @Test
    fun `when typed empty string then no error and button disabled`() {
        viewModel.initialize(labels)

        viewModel.onEmailChanged("")

        val state = viewModel.viewState.value!!
        assertFalse(state.isError)
        assertFalse(state.isButtonEnabled)
        assertEquals("", state.errorMessage)
    }

    @Test
    fun `when typed email missing at sign then isError is true`() {
        viewModel.initialize(labels)

        viewModel.onEmailChanged("invalidemail.com")

        val state = viewModel.viewState.value!!
        assertTrue(state.isError)
        assertFalse(state.isButtonEnabled)
    }

    @Test
    fun `when typed email missing domain then isError is true`() {
        viewModel.initialize(labels)

        viewModel.onEmailChanged("user@")

        val state = viewModel.viewState.value!!
        assertTrue(state.isError)
        assertFalse(state.isButtonEnabled)
    }

    @Test
    fun `when typed email missing local part then isError is true`() {
        viewModel.initialize(labels)

        viewModel.onEmailChanged("@example.com")

        val state = viewModel.viewState.value!!
        assertTrue(state.isError)
        assertFalse(state.isButtonEnabled)
    }

    @Test
    fun `when typed only spaces then no error and button disabled`() {
        viewModel.initialize(labels)

        viewModel.onEmailChanged("   ")

        val state = viewModel.viewState.value!!
        assertFalse(state.isError)
        assertFalse(state.isButtonEnabled)
        assertEquals("", state.errorMessage)
    }

    // endregion

    // region errorMessage in state

    @Test
    fun `when no error then errorMessage is empty`() {
        viewModel.initialize(labels, baseEmail = "user@example.com")

        assertEquals("", viewModel.viewState.value!!.errorMessage)
    }

    @Test
    fun `when email goes back to blank after invalid input then errorMessage is empty`() {
        viewModel.initialize(labels)
        viewModel.onEmailChanged("invalid-email")
        viewModel.onEmailChanged("")

        assertEquals("", viewModel.viewState.value!!.errorMessage)
    }

    @Test
    fun `when email is invalid format then errorMessage returns errorEmailInvalid`() {
        viewModel.initialize(labels)
        viewModel.onEmailChanged("invalid-email")

        assertEquals(labels.errorEmailInvalid, viewModel.viewState.value!!.errorMessage)
    }

    @Test
    fun `when email is invalid with missing domain then errorMessage returns errorEmailInvalid`() {
        viewModel.initialize(labels)
        viewModel.onEmailChanged("user@")

        assertEquals(labels.errorEmailInvalid, viewModel.viewState.value!!.errorMessage)
    }

    // endregion
}
