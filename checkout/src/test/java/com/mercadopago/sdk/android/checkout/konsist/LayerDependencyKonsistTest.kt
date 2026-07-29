package com.mercadopago.sdk.android.checkout.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import org.junit.Test

/**
 * Direcao de dependencia entre camadas (clean arch).
 * foundation e a base; components depende de foundation; checkout depende de ambos —
 * nunca o contrario. Ajuste os pacotes se a topologia mudar.
 */
class LayerDependencyKonsistTest {
    @Test
    fun `camadas respeitam a direcao de dependencia`() {
        Konsist.scopeFromProject().assertArchitecture {
            val foundation = Layer("Foundation", "com.mercadopago.sdk.android.foundation..")
            val components = Layer("Components", "com.mercadopago.sdk.android.components..")
            val checkout = Layer("Checkout", "com.mercadopago.sdk.android.checkout..")

            foundation.dependsOnNothing()
            components.dependsOn(foundation)
            checkout.dependsOn(components, foundation)
        }
    }
}
