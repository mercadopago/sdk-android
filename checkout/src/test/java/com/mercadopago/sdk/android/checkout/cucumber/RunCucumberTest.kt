package com.mercadopago.sdk.android.checkout.cucumber

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

/**
 * Runner Cucumber (JUnit4). Executa os .feature de src/test/resources/features.
 * Roda com `./gradlew :checkout:testDebugUnitTest`.
 */
@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src/test/resources/features"],
    glue = ["com.mercadopago.sdk.android.checkout.cucumber"],
    plugin = ["pretty"],
)
class RunCucumberTest
