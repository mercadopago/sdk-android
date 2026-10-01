package com.mercadopago.sdk.android.checkout.behavior

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

/**
 * BDD (given/when/then) do contrato de resultado do checkout.
 * Ilustrativo e auto-contido — ligue [CheckoutResult] ao tipo real do SDK
 * (Success / Error / UserCancelled) para prender regressao de contrato.
 *
 * Requer JUnit Platform (Kotest 5) — ver VALIDATION.md.
 */
class CheckoutResultBehaviorSpec : BehaviorSpec({

    given("um checkout em andamento") {
        `when`("o pagamento e aprovado") {
            then("o resultado e Success") {
                resolve(Event.APPROVED) shouldBe CheckoutResult.Success
            }
        }
        `when`("o usuario cancela") {
            then("o resultado e UserCancelled") {
                resolve(Event.CANCELLED) shouldBe CheckoutResult.UserCancelled
            }
        }
        `when`("ha falha de rede") {
            then("o resultado e Error") {
                resolve(Event.NETWORK_FAILURE) shouldBe CheckoutResult.Error
            }
        }
    }
})

private enum class Event { APPROVED, CANCELLED, NETWORK_FAILURE }

private sealed interface CheckoutResult {
    data object Success : CheckoutResult

    data object Error : CheckoutResult

    data object UserCancelled : CheckoutResult
}

private fun resolve(
    event: Event,
): CheckoutResult = when (event) {
    Event.APPROVED -> CheckoutResult.Success
    Event.CANCELLED -> CheckoutResult.UserCancelled
    Event.NETWORK_FAILURE -> CheckoutResult.Error
}
