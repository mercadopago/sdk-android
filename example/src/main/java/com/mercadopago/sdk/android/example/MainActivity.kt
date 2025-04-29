package com.mercadopago.sdk.android.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mercadopago.sdk.android.example.screens.PaymentExampleScreen
import com.mercadopago.sdk.android.example.ui.theme.ExampleTheme

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExampleTheme {
                PaymentExampleScreen()
            }
        }
    }
}
