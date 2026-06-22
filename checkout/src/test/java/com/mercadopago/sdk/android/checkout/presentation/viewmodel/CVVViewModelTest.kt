package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class CVVViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun buildScreen(
        headerTitle: String = "Ingresá el código de seguridad",
        label: String = "Código de seguridad",
        placeholder: String = "Ej.: 123",
        helper: String = "Está en el reverso de tu tarjeta.",
        continueButtonLabel: String = "Continuar",
    ) = SecurityCodeScreenOutput(
        headerTitle = headerTitle,
        field = SecurityCodeFieldOutput(label = label, placeholder = placeholder, helper = helper),
        continueButtonLabel = continueButtonLabel,
    )

    private fun viewModel(
        expectedLength: Int = 3,
    ) =
        CVVViewModel(securityCodeScreen = buildScreen(), cvvExpectedLength = expectedLength)

    @Test
    fun `given initial state then screenData is populated from securityCodeScreen`() {
        val vm = viewModel()

        assertNotNull(vm.viewState.value.screenData)
        assertEquals("Ingresá el código de seguridad", vm.viewState.value.screenData?.headerTitle)
        assertEquals("Continuar", vm.viewState.value.screenData?.continueButtonLabel)
    }

    @Test
    fun `given initial state then cvvLength is zero and continue is disabled`() {
        val vm = viewModel()

        assertEquals(0, vm.viewState.value.cvvLength)
        assertFalse(vm.viewState.value.isContinueEnabled)
    }

    @Test
    fun `given matching CVV length then isContinueEnabled is true`() {
        val vm = viewModel(expectedLength = 3)

        vm.onCVVLengthChanged(3)

        assertTrue(vm.viewState.value.isContinueEnabled)
        assertEquals(3, vm.viewState.value.cvvLength)
    }

    @Test
    fun `given length too short then isContinueEnabled is false`() {
        val vm = viewModel(expectedLength = 3)

        vm.onCVVLengthChanged(2)

        assertFalse(vm.viewState.value.isContinueEnabled)
    }

    @Test
    fun `given length too long then isContinueEnabled is false`() {
        val vm = viewModel(expectedLength = 3)

        vm.onCVVLengthChanged(4)

        assertFalse(vm.viewState.value.isContinueEnabled)
    }

    @Test
    fun `given valid length then errorMessage is null`() {
        val vm = viewModel(expectedLength = 3)

        vm.onCVVLengthChanged(3)

        assertNull(vm.viewState.value.errorMessage)
    }

    @Test
    fun `given CVV length changed from valid to invalid then isContinueEnabled reverts to false`() {
        val vm = viewModel(expectedLength = 3)
        vm.onCVVLengthChanged(3)

        vm.onCVVLengthChanged(2)

        assertFalse(vm.viewState.value.isContinueEnabled)
    }

    @Test
    fun `given onContinue with valid CVV length then isContinueEnabled remains true`() {
        val vm = viewModel(expectedLength = 3)
        vm.onCVVLengthChanged(3)

        vm.onContinue()

        assertTrue(vm.viewState.value.isContinueEnabled)
    }

    @Test
    fun `given onContinue with invalid CVV length then isContinueEnabled is false`() {
        val vm = viewModel(expectedLength = 3)
        vm.onCVVLengthChanged(2)

        vm.onContinue()

        assertFalse(vm.viewState.value.isContinueEnabled)
    }

    @Test
    fun `given 4-digit card then length 4 enables continue`() {
        val vm = viewModel(expectedLength = 4)

        vm.onCVVLengthChanged(4)

        assertTrue(vm.viewState.value.isContinueEnabled)
    }

    @Test
    fun `given 4-digit card then length 3 does not enable continue`() {
        val vm = viewModel(expectedLength = 4)

        vm.onCVVLengthChanged(3)

        assertFalse(vm.viewState.value.isContinueEnabled)
    }
}
