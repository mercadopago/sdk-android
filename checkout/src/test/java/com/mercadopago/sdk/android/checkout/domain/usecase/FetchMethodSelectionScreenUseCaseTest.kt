package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenFooter
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class FetchMethodSelectionScreenUseCaseTest {
    private val useCase = FetchMethodSelectionScreenUseCase()

    private val screenData = MethodSelectionScreenData(
        headerTitle = "Escolha o boleto",
        selectionType = SelectionDisplayType.Chevron,
        footer = MethodSelectionScreenFooter(totalLabel = "Total", totalAmount = "R$ 100,00"),
        options = listOf(
            MethodSelectionOption(id = "boleto", name = "Boleto", subtitle = "3 dias", iconUrl = "url"),
        ),
    )

    private fun makeMethod(
        screen: MethodSelectionScreenData?,
    ) = PaymentMethodOutput(
        type = "ticket",
        title = "Boleto",
        screen = screen,
    )

    @Test
    fun `given method with screen when invoked then returns screen`() {
        val method = makeMethod(screen = screenData)

        val result = useCase(method)

        assertNotNull(result)
    }

    @Test
    fun `given method without screen when invoked then returns null`() {
        val method = makeMethod(screen = null)

        val result = useCase(method)

        assertNull(result)
    }
}
