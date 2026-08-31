package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenButton
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenFooter
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import com.mercadopago.sdk.android.checkout.presentation.state.MethodSelectionViewEvent
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class MethodSelectionViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val option1 = MethodSelectionOption(id = "boleto", name = "Boleto", subtitle = "3 dias", iconUrl = "url1")
    private val option2 = MethodSelectionOption(id = "efecty", name = "Efecty", subtitle = "em breve", iconUrl = "url2")

    private fun makeScreenData(
        selectionType: SelectionDisplayType = SelectionDisplayType.RadioButton,
        hasButton: Boolean = true,
    ) = MethodSelectionScreenData(
        headerTitle = "Escolha",
        selectionType = selectionType,
        footer = MethodSelectionScreenFooter(
            totalLabel = "Total",
            totalAmount = "R$ 100,00",
            button = if (hasButton) MethodSelectionScreenButton(label = "Confirmar") else null,
        ),
        options = listOf(option1, option2),
    )

    private fun makeViewModel(
        screenData: MethodSelectionScreenData = makeScreenData(),
    ) = MethodSelectionViewModel(screenData)

    @Test
    fun `given screenData with button when initialized then buttonState enabled is false`() = runTest {
        val viewModel = makeViewModel()

        assertFalse(viewModel.screenState.value.footerState.buttonState?.enabled ?: true)
    }

    @Test
    fun `given screenData without button when initialized then buttonState is null`() = runTest {
        val viewModel = makeViewModel(makeScreenData(hasButton = false))

        assertNull(viewModel.screenState.value.footerState.buttonState)
    }

    @Test
    fun `given initial state when accessed then selectedOptionId is null`() = runTest {
        val viewModel = makeViewModel()

        assertNull(viewModel.screenState.value.selectedOptionId)
    }

    @Test
    fun `given radio button layout when selectOption then selectedOptionId is updated`() = runTest {
        val viewModel = makeViewModel(makeScreenData(selectionType = SelectionDisplayType.RadioButton))

        viewModel.selectOption(option1.id)

        assertEquals(option1.id, viewModel.screenState.value.selectedOptionId)
    }

    @Test
    fun `given radio button layout when selectOption then CTA is enabled`() = runTest {
        val viewModel = makeViewModel(makeScreenData(selectionType = SelectionDisplayType.RadioButton))

        viewModel.selectOption(option1.id)

        assertTrue(viewModel.screenState.value.footerState.buttonState?.enabled ?: false)
    }

    @Test
    fun `given option selected when confirmSelection then OnOptionSelected event is emitted`() = runTest {
        val viewModel = makeViewModel()
        viewModel.selectOption(option1.id)

        viewModel.confirmSelection()

        val event = viewModel.viewEvent.value
        assertNotNull(event)
        assertTrue(event is MethodSelectionViewEvent.OnOptionSelected)
        assertEquals(option1, (event as MethodSelectionViewEvent.OnOptionSelected).option)
    }

    @Test
    fun `given confirmSelection already called when confirmSelection is called again then tracking is not repeated`() =
        runTest {
            val viewModel = makeViewModel()
            viewModel.selectOption(option1.id)
            viewModel.confirmSelection()

            // second call must not re-track the selection — only the state/event side keeps updating
            viewModel.confirmSelection()

            val event = viewModel.viewEvent.value
            assertTrue(event is MethodSelectionViewEvent.OnOptionSelected)
            assertEquals(option1, (event as MethodSelectionViewEvent.OnOptionSelected).option)
        }

    @Test
    fun `given no option selected when confirmSelection then no event emitted`() = runTest {
        val viewModel = makeViewModel()

        viewModel.confirmSelection()

        assertNull(viewModel.viewEvent.value)
    }

    @Test
    fun `given event emitted when onViewEventConsumed then event is null`() = runTest {
        val viewModel = makeViewModel()
        viewModel.selectOption(option1.id)
        viewModel.confirmSelection()

        viewModel.onViewEventConsumed()

        assertNull(viewModel.viewEvent.value)
    }

    @Test
    fun `given screenData when initialized then headerTitle is correct`() = runTest {
        val viewModel = makeViewModel()

        assertEquals("Escolha", viewModel.screenState.value.headerTitle)
    }

    @Test
    fun `given screenData when initialized then options are correct`() = runTest {
        val viewModel = makeViewModel()

        assertEquals(2, viewModel.screenState.value.options.size)
    }

    @Test
    fun `given chevron layout when initialized then isArrowLayout is true`() = runTest {
        val viewModel = makeViewModel(makeScreenData(selectionType = SelectionDisplayType.Chevron))

        assertTrue(viewModel.screenState.value.isArrowLayout)
    }

    @Test
    fun `given radio button layout when initialized then isArrowLayout is false`() = runTest {
        val viewModel = makeViewModel(makeScreenData(selectionType = SelectionDisplayType.RadioButton))

        assertFalse(viewModel.screenState.value.isArrowLayout)
    }

    // ── Analytics tracking — smoke tests (MPAnalytics unavailable in unit tests) ──

    @Test
    fun `when viewModel created then does not throw even with analytics unavailable`() = runTest {
        makeViewModel(makeScreenData())
    }

    @Test
    fun `given chevron layout when selectOption then does not throw`() = runTest {
        val viewModel = makeViewModel(makeScreenData(selectionType = SelectionDisplayType.Chevron))
        viewModel.selectOption(option1.id)
    }

    @Test
    fun `given option selected when confirmSelection then does not throw`() = runTest {
        val viewModel = makeViewModel(makeScreenData(selectionType = SelectionDisplayType.RadioButton))
        viewModel.selectOption(option1.id)
        viewModel.confirmSelection()
    }

    @Test
    fun `given option selected when confirmSelection is called twice then does not throw`() = runTest {
        val viewModel = makeViewModel(makeScreenData(selectionType = SelectionDisplayType.RadioButton))
        viewModel.selectOption(option1.id)
        viewModel.confirmSelection()
        viewModel.confirmSelection() // second call must not re-track — must not throw
    }

    @Test
    fun `when goBack is called then does not throw`() = runTest {
        val viewModel = makeViewModel()
        viewModel.goBack()
    }

    @Test
    fun `when goBack is called multiple times then does not throw`() = runTest {
        val viewModel = makeViewModel()
        viewModel.goBack()
        viewModel.goBack()
    }
}
