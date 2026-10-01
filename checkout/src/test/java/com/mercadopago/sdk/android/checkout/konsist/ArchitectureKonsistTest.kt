package com.mercadopago.sdk.android.checkout.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

/**
 * Fitness functions de arquitetura — transforma os forge-rules/ (prosa) em GATE.
 * Roda em JUnit4 (sem exigir JUnit5), entao convive com a suite atual.
 *
 * Cada teste codifica uma regra que hoje esta so documentada. Ajuste os limiares/
 * pacotes conforme o time evoluir os forge-rules.
 */
class ArchitectureKonsistTest {
    private val project = Konsist.scopeFromProject()

    /** forge-rules/android.imports: este SDK usa CrabDI/Koin — nunca Hilt. */
    @Test
    fun `nenhum arquivo importa Hilt`() {
        project.files.assertFalse { file ->
            file.hasImport { it.name.startsWith("dagger.hilt") }
        }
    }

    /** forge-rules/android.imports: AndesUI web nao e usado neste SDK. */
    @Test
    fun `nenhum arquivo importa AndesUI web`() {
        project.files.assertFalse { file ->
            file.hasImport { it.name.startsWith("com.mercadolibre.android.andesui") }
        }
    }

    /**
     * "codigo escrito para a IA ler": classe/interface publica precisa de KDoc.
     * Escopo: modulo checkout (producao), exclui testes e example.
     */
    @Test
    fun `classe publica tem KDoc`() {
        project.classesAndInterfaces()
            .filter { it.hasPublicOrDefaultModifier }
            .filter { it.resideInPackage("com.mercadopago.sdk.android.checkout..") }
            .filter { !it.containingFile.path.contains("/test/") }
            .assertTrue { it.hasKDoc }
    }

    /**
     * forge-rules/android.naming: callbacks de mudanca de campo seguem on{Campo}Change.
     * Heuristica: parametros cujo nome comeca com "on" e termina em "Changed" (errado)
     * devem ser renomeados para terminar em "Change". Exclui nomes do framework Compose
     * (onFocusChanged) e o modulo example.
     */
    @Test
    fun `callbacks de mudanca seguem padrao onXxxChange`() {
        val composeFrameworkCallbacks = setOf("onFocusChanged", "onGloballyPositioned")
        project.functions()
            .filter { it.resideInPackage("com.mercadopago.sdk.android..") }
            .filter { !it.resideInPackage("..example..") }
            .flatMap { it.parameters }
            .filter { it.name.startsWith("on") && it.name.endsWith("Changed") }
            .filter { it.name !in composeFrameworkCallbacks }
            .assertTrue { it.name.endsWith("Change") }
    }

    /**
     * Prefixo MP na API publica de componentes (forge-rules/andes.*).
     * Escopo: Composables publicos do modulo :components (SDK), nao do app example.
     */
    @Test
    fun `composable publico de components tem prefixo MP`() {
        project.functions()
            .filter { it.resideInPackage("com.mercadopago.sdk.android.components..") }
            .filter { it.hasPublicOrDefaultModifier }
            .filter { it.hasAnnotation { a -> a.name == "Composable" } }
            .filter { fn -> fn.name.first().isUpperCase() }
            .assertTrue { it.name.startsWith("MP") }
    }
}
