package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.EmailInitializationOutput
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

    @Before
    fun setup() {
        viewModel = EmailViewModel()
    }

    private fun initialize(
        prefilledEmail: String? = null,
    ) {
        viewModel.initialize(
            EmailInitializationOutput(
                title = "Title",
                buttonLabel = "Continue",
                fieldLabel = "Email",
                fieldPlaceholder = "placeholder@email.com",
                errorFieldEmpty = "Field is empty",
                errorEmailInvalid = "Invalid email",
                prefilledEmail = prefilledEmail,
            ),
        )
    }

    @Test
    fun `when not initialized then viewState is null`() {
        assertNull(viewModel.viewState.value)
    }

    @Test
    fun `when initialize with null prefilledEmail then state has empty email and button disabled`() {
        initialize(prefilledEmail = null)

        val state = viewModel.viewState.value!!
        assertEquals("", state.fieldState.value)
        assertFalse(state.isButtonEnabled)
        assertFalse(state.fieldState.isValid)
        assertEquals("", state.fieldState.error)
    }

    @Test
    fun `when initialize with valid prefilledEmail then button is enabled`() {
        initialize(prefilledEmail = "user@example.com")

        val state = viewModel.viewState.value!!
        assertEquals("user@example.com", state.fieldState.value)
        assertTrue(state.isButtonEnabled)
        assertTrue(state.fieldState.isValid)
        assertEquals("", state.fieldState.error)
    }

    @Test
    fun `when initialize with invalid prefilledEmail then button is disabled and no error shown`() {
        initialize(prefilledEmail = "not-an-email")

        val state = viewModel.viewState.value!!
        assertFalse(state.isButtonEnabled)
        assertFalse(state.fieldState.isValid)
        assertEquals("", state.fieldState.error)
    }

    @Test
    fun `when typed valid email then button is enabled and no error`() {
        initialize()

        viewModel.onEmailChanged("user@example.com")

        val state = viewModel.viewState.value!!
        assertTrue(state.isButtonEnabled)
        assertTrue(state.fieldState.isValid)
        assertEquals("", state.fieldState.error)
    }

    @Test
    fun `when typed valid email with subdomain then button is enabled`() {
        initialize()

        viewModel.onEmailChanged("user@mail.example.com")

        assertTrue(viewModel.viewState.value!!.isButtonEnabled)
    }

    @Test
    fun `when typed valid email with plus sign then button is enabled`() {
        initialize()

        viewModel.onEmailChanged("user+tag@example.com")

        assertTrue(viewModel.viewState.value!!.isButtonEnabled)
    }

    @Test
    fun `when typed empty string then shows empty error and button disabled`() {
        initialize()

        viewModel.onEmailChanged("")

        val state = viewModel.viewState.value!!
        assertTrue(state.fieldState.error.isNotEmpty())
        assertFalse(state.isButtonEnabled)
        assertEquals("Field is empty", state.fieldState.error)
    }

    @Test
    fun `when typed email missing at sign then error is set`() {
        initialize()

        viewModel.onEmailChanged("invalidemail.com")

        val state = viewModel.viewState.value!!
        assertTrue(state.fieldState.error.isNotEmpty())
        assertFalse(state.isButtonEnabled)
    }

    @Test
    fun `when typed email missing domain then error is set`() {
        initialize()

        viewModel.onEmailChanged("user@")

        val state = viewModel.viewState.value!!
        assertTrue(state.fieldState.error.isNotEmpty())
        assertFalse(state.isButtonEnabled)
    }

    @Test
    fun `when typed email missing local part then error is set`() {
        initialize()

        viewModel.onEmailChanged("@example.com")

        val state = viewModel.viewState.value!!
        assertTrue(state.fieldState.error.isNotEmpty())
        assertFalse(state.isButtonEnabled)
    }

    @Test
    fun `when typed only spaces then shows empty error and button disabled`() {
        initialize()

        viewModel.onEmailChanged("   ")

        val state = viewModel.viewState.value!!
        assertFalse(state.isButtonEnabled)
        assertEquals("Field is empty", state.fieldState.error)
    }

    @Test
    fun `when no error then error field is empty`() {
        initialize(prefilledEmail = "user@example.com")

        assertEquals("", viewModel.viewState.value!!.fieldState.error)
    }

    @Test
    fun `when email goes back to blank after invalid input then shows empty error`() {
        initialize()
        viewModel.onEmailChanged("invalid-email")
        viewModel.onEmailChanged("")

        assertEquals("Field is empty", viewModel.viewState.value!!.fieldState.error)
    }

    @Test
    fun `when email is invalid format then error returns invalid message`() {
        initialize()
        viewModel.onEmailChanged("invalid-email")

        assertEquals("Invalid email", viewModel.viewState.value!!.fieldState.error)
    }

    @Test
    fun `when email is invalid with missing domain then error returns invalid message`() {
        initialize()
        viewModel.onEmailChanged("user@")

        assertEquals("Invalid email", viewModel.viewState.value!!.fieldState.error)
    }

    @Test
    fun `when focus changes to true then isFocused is updated in state`() {
        initialize()

        viewModel.onFocusChanged(true)

        assertTrue(viewModel.viewState.value!!.fieldState.isFocused)
    }

    @Test
    fun `when focus changes to false then isFocused is false in state`() {
        initialize()
        viewModel.onFocusChanged(true)
        viewModel.onFocusChanged(false)

        assertFalse(viewModel.viewState.value!!.fieldState.isFocused)
    }
}
