package com.mercadopago.sdk.android.checkout.presentation.controller

import android.content.Context
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.interactor.Checkout
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoDefaultThemes
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeAppearance
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Test
import org.koin.core.Koin
import kotlin.test.assertNotNull

internal class CheckoutControllerTest {
    private val context = mockk<Context>(relaxed = true)
    private val koin = mockk<Koin>()
    private val checkoutThemePreferences = mockk<CheckoutThemePreferences>(relaxed = true)

    @Test
    fun `when create is called Then return CheckoutController`() {
        // Given
        mockkObject(Checkout.Companion)
        val theme = MercadoPagoDefaultThemes.Default
        val appearance = MercadoPagoThemeAppearance.Light
        every {
            Checkout.getInstance().koin
        } returns koin
        every {
            koin.get<CheckoutThemePreferences>()
        } returns checkoutThemePreferences

        // When
        val checkoutController = CheckoutController.create(
            context = context,
            theme = theme,
            appearance = appearance,
        )

        // Then
        assertNotNull(checkoutController)
    }

    @Test
    fun `when launchBottomSheet is called Then set theme and launch intent`() {
        // Given
        mockkObject(Checkout.Companion)
        val theme = MercadoPagoDefaultThemes.Default
        val appearance = MercadoPagoThemeAppearance.Light
        every {
            Checkout.getInstance().koin
        } returns koin
        every {
            koin.get<CheckoutThemePreferences>()
        } returns checkoutThemePreferences

        // When
        val checkoutController = CheckoutController.create(
            context = context,
            theme = theme,
            appearance = appearance,
        )
        checkoutController.launchCheckout()

        // Then
        verify(exactly = 1) {
            checkoutThemePreferences.setCurrentAppearance(appearance)
            checkoutThemePreferences.setCurrentThemeScheme(theme)
            context.startActivity(any())
        }
        assertNotNull(checkoutController)
    }
}
