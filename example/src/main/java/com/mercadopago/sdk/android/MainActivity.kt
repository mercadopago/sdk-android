package com.mercadopago.sdk.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mercadopago.sdk.android.screens.PaymentExampleScreen
import com.mercadopago.sdk.android.ui.theme.ExampleTheme

class MainActivity : ComponentActivity() {
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
