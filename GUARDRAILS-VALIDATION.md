# Guard rails — lote gradle (validação local)

Este lote foi **escrito mas não compilado** (o ambiente onde foi gerado não tem Android SDK).
Valide com `./gradlew` local antes de commitar. Toda alteração de build tem backup `.bak`.

## Arquivos alterados
- `gradle/libs.versions.toml` (+ `.bak`) — versões/libs/plugins de guard rails.
- `build.gradle.kts` (root, + `.bak`) — plugin `binary-compatibility-validator` + bloco `apiValidation`.
- `checkout/build.gradle.kts` (+ `.bak`) — deps de teste: Konsist, Kotest, Cucumber.

## Arquivos novos
- `checkout/src/test/.../konsist/ArchitectureKonsistTest.kt`, `LayerDependencyKonsistTest.kt`
- `checkout/src/test/.../cucumber/RunCucumberTest.kt`, `SellerCheckoutSteps.kt`
- `checkout/src/test/resources/features/seller_checkout.feature`
- `checkout/src/test/.../property/CardNumberPropertySpec.kt`
- `checkout/src/test/.../behavior/CheckoutResultBehaviorSpec.kt`

---

## 1. Binary compatibility (apiCheck) — pronto
```bash
./gradlew apiDump      # 1x: gera os arquivos .api da API pública atual (commitar junto)
./gradlew apiCheck     # gate: falha se a API pública mudar sem novo apiDump
```
Caveat: o `nonPublicMarkers` no root aponta uma annotation **placeholder**
(`...foundation.annotations.InternalMpApi`). Remova a linha ou crie a annotation.

## 2. Konsist — pronto (roda em JUnit4)
```bash
./gradlew :checkout:testDebugUnitTest --tests "*Konsist*"
```
Ajuste versão se necessário (`konsist = "0.17.3"`). As regras codificam os `forge-rules/`
(sem Hilt, sem AndesUI web, KDoc em classe pública, naming onXxxChange, prefixo MP, direção de dependência).

## 3. Cucumber — pronto (roda em JUnit4)
```bash
./gradlew :checkout:testDebugUnitTest --tests "*RunCucumberTest*"
```
Ligue os steps (`SellerCheckoutSteps`) à borda pública real do checkout (hoje há um `FakeCheckout`).

## 4. Kotest (property + BehaviorSpec) — requer JUnit Platform
Kotest 5 roda no **JUnit Platform (JUnit5)**; o `checkout` hoje usa JUnit4. Para coexistir,
adicione no `checkout/build.gradle.kts`:
```kotlin
// dentro de android { } ou no top-level:
tasks.withType<Test> { useJUnitPlatform() }
// e, para os testes JUnit4 legados continuarem rodando na plataforma:
dependencies { testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.10.2") }
```
Valide os JUnit4 existentes + Kotest juntos:
```bash
./gradlew :checkout:testDebugUnitTest
```
Se houver atrito com Robolectric/JUnit4, isole os specs Kotest em um módulo de teste separado.

## 5. Pitest (mutation) — snippet (não aplicado; é o de maior risco em Android)
Escolhido o plugin Android `pl.droidsonroids.pitest`. Adicione ao `checkout/build.gradle.kts`:
```kotlin
plugins { alias(libs.plugins.droidsonroids.pitest) }

pitest {
    targetClasses.set(listOf("com.mercadopago.sdk.android.checkout.domain.*"))
    excludedClasses.set(listOf("*Composable*", "*Preview*", "*Kt"))
    mutationThreshold.set(60)      // GATE: build falha abaixo do mutation score
    coverageThreshold.set(0)
    threads.set(4)
    // junit5PluginVersion.set("1.2.1")  // se migrar os testes p/ JUnit5
}
```
```bash
./gradlew :checkout:pitestDebug
```
Caveat: confirme a versão do plugin vs AGP 8.7.3. Comece com `mutationThreshold` baixo
e suba gradualmente. Depois de estável, pluge no Stop gate (ver abaixo).

## Plugar Pitest no Stop gate (opcional, depois de estável)
Em `.claude/scripts/tdd-gate.sh`, após o bloco de teste/cobertura, adicione a task
`pitestDebug` aos módulos afetados (guardada por env `TDD_GATE_MUTATION=1`).

---

## Rollback
```bash
git checkout -- gradle/libs.versions.toml build.gradle.kts checkout/build.gradle.kts
# ou restaurar os .bak:
mv gradle/libs.versions.toml.bak gradle/libs.versions.toml
mv build.gradle.kts.bak build.gradle.kts
mv checkout/build.gradle.kts.bak checkout/build.gradle.kts
```
