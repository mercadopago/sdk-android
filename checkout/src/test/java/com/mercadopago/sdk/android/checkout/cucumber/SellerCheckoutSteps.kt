package com.mercadopago.sdk.android.checkout.cucumber

import io.cucumber.java.pt.Dado
import io.cucumber.java.pt.Entao
import io.cucumber.java.pt.Quando
import org.junit.Assert.assertEquals

/**
 * Steps do contrato do seller. Este e o ponto de ligacao com o SDK real:
 * substitua o [FakeCheckout] pela borda publica de checkout (MPCheckout / MPCheckoutType)
 * e mapeie os resultados para os tipos reais Success / Error / UserCancelled.
 */
class SellerCheckoutSteps {
    private var resultLabel: String = ""

    // Substitua FakeCheckout pelo acionamento real do checkout do SDK.
    private object FakeCheckout {
        const val APPROVE = "Success"
        const val CANCEL_ON_FIRST_SCREEN = "UserCancelled"
        const val NETWORK_FAILURE = "Error"
    }

    @Dado("um checkout iniciado com um meio de pagamento valido")
    fun checkoutIniciado() {
        resultLabel = ""
    }

    @Quando("o pagamento e aprovado")
    fun pagamentoAprovado() {
        resultLabel = FakeCheckout.APPROVE
    }

    @Quando("o usuario cancela na tela inicial")
    fun usuarioCancela() {
        resultLabel = FakeCheckout.CANCEL_ON_FIRST_SCREEN
    }

    @Quando("ocorre uma falha de rede")
    fun falhaDeRede() {
        resultLabel = FakeCheckout.NETWORK_FAILURE
    }

    @Entao("o resultado deve ser {string}")
    fun resultadoDeveSer(
        esperado: String,
    ) {
        assertEquals(esperado, resultLabel)
    }
}
