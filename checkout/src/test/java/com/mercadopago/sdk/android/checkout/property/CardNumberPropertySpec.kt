package com.mercadopago.sdk.android.checkout.property

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll

/**
 * Property-based testing: gera milhares de entradas e expoe edge cases que teste
 * manual nao cobre. Aqui validamos a propriedade de Luhn de forma auto-contida.
 *
 * PARA VALER DE VERDADE: aponte estas properties para o validador real do SDK
 * (ex.: IsCardNumberValidUseCase) em vez do [luhnValid] local.
 *
 * Requer JUnit Platform (Kotest 5) — ver VALIDATION.md.
 */
class CardNumberPropertySpec : StringSpec({

    "numero valido continua valido apos adicionar o digito verificador de Luhn" {
        checkAll(Arb.long(0L, 9_999_999_999_999L)) { base ->
            val withCheck = appendLuhnCheckDigit(base.toString())
            luhnValid(withCheck) shouldBe true
        }
    }

    "alterar um unico digito quebra a validade (na maioria dos casos)" {
        checkAll(Arb.long(1_000_000L, 9_999_999L), Arb.int(0, 6)) { base, pos ->
            val valid = appendLuhnCheckDigit(base.toString())
            val flipped = flipDigit(valid, pos % valid.length)
            if (flipped != valid) {
                // Luhn detecta 100% dos erros de 1 digito
                luhnValid(flipped) shouldBe false
            }
        }
    }
})

private fun luhnValid(
    number: String,
): Boolean {
    if (number.isEmpty() || number.any { !it.isDigit() }) return false
    var sum = 0
    var alt = false
    for (i in number.length - 1 downTo 0) {
        var d = number[i] - '0'
        if (alt) {
            d *= 2
            if (d > 9) d -= 9
        }
        sum += d
        alt = !alt
    }
    return sum % 10 == 0
}

private fun appendLuhnCheckDigit(
    digits: String,
): String {
    val partial = digits + "0"
    var sum = 0
    var alt = true
    for (i in partial.length - 1 downTo 0) {
        var d = partial[i] - '0'
        if (alt) {
            d *= 2
            if (d > 9) d -= 9
        }
        sum += d
        alt = !alt
    }
    val check = (10 - (sum % 10)) % 10
    return digits + check
}

private fun flipDigit(
    number: String,
    index: Int,
): String {
    val c = number[index]
    val newDigit = if (c == '9') '0' else c + 1
    return number.substring(0, index) + newDigit + number.substring(index + 1)
}
