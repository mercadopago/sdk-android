package com.mercadopago.sdk.android.checkout.core

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutAppearance
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.di.CheckoutModulesProvider
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.interactor.Checkout
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoUserInterfaceStyle
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.Koin
import kotlin.test.assertNotNull
import kotlin.test.assertSame

internal class MercadoPagoCheckoutTest {
    private val context = mockk<Context>(relaxed = true)
    private val checkoutType = MPCheckoutType.CardSave
    private val mockThemePreferences = mockk<CheckoutThemePreferences>(relaxed = true)
    private val mockKoin = mockk<Koin>(relaxed = true)

    @Before
    fun setUp() {
        mockkObject(CheckoutCallbackHolder)
        mockkConstructor(CheckoutModulesProvider::class, Intent::class)
        every { CheckoutCallbackHolder.setCallback<MPPaymentData.CardSave>(any()) } just Runs
        every { anyConstructed<CheckoutModulesProvider>().koinApp } returns mockKoin
        every { mockKoin.get<CheckoutThemePreferences>() } returns mockThemePreferences
        every { anyConstructed<Intent>().putExtra(any<String>(), any<Parcelable>()) } returns mockk(relaxed = true)
        Checkout.clearInstance()
    }

    @After
    fun tearDown() {
        Checkout.clearInstance()
        unmockkAll()
    }

    @Test
    fun `when build called then returns MercadoPagoCheckout instance`() {
        val checkout = MercadoPagoCheckout.Builder(context, checkoutType).build()

        assertNotNull(checkout)
    }

    @Test
    fun `when setPaymentMethods called then returns same builder instance`() {
        val builder = MercadoPagoCheckout.Builder(context, checkoutType)

        val result = builder.setPaymentMethods()

        assertSame(builder, result)
    }

    @Test
    fun `when show called then setCallback is called with provided callback`() {
        val checkout = buildCheckout()
        val callback: (MercadoPagoCheckoutResult<MPPaymentData.CardSave>) -> Unit = {}

        checkout.show(callback)

        verify { CheckoutCallbackHolder.setCallback(callback) }
    }

    @Test
    fun `when show called then startActivity is invoked`() {
        val checkout = buildCheckout()

        checkout.show {}

        verify { context.startActivity(any()) }
    }

    @Test
    fun `when show called with appearance then sets style from appearance`() {
        val style = MercadoPagoUserInterfaceStyle.Dark
        val checkout = buildCheckout(appearance = MPCheckoutAppearance(style = style))

        checkout.show {}

        verify { mockThemePreferences.setCurrentStyle(style) }
    }

    @Test
    fun `when show called with appearance then sets theme from appearance`() {
        val theme = MercadoPagoThemes.Default
        val checkout = buildCheckout(appearance = MPCheckoutAppearance(theme = theme))

        checkout.show {}

        verify { mockThemePreferences.setCurrentThemeScheme(theme) }
    }

    @Test
    fun `when show called with null appearance then sets system style`() {
        val checkout = buildCheckout(appearance = null)

        checkout.show {}

        verify { mockThemePreferences.setCurrentStyle(MercadoPagoUserInterfaceStyle.System) }
    }

    @Test
    fun `when show called with null appearance then sets default theme`() {
        val checkout = buildCheckout(appearance = null)

        checkout.show {}

        verify { mockThemePreferences.setCurrentThemeScheme(MercadoPagoThemes.Default) }
    }

    private fun buildCheckout(
        appearance: MPCheckoutAppearance? = MPCheckoutAppearance(),
    ) = MercadoPagoCheckout.Builder(
        context = context,
        checkoutType = checkoutType,
        checkoutAppearance = appearance,
    ).build()
}
