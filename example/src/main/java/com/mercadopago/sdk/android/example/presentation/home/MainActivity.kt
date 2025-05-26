package com.mercadopago.sdk.android.example.presentation.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme

internal class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MercadoPagoSampleTheme {
                SampleHomeScreen()
            }
        }
    }
}
