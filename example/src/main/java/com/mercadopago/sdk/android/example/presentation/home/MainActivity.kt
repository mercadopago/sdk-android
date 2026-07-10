package com.mercadopago.sdk.android.example.presentation.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme

internal class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MercadoPagoSampleTheme {
                Box(Modifier.semantics { testTagsAsResourceId = true }) {
                    SampleHomeScreen()
                }
            }
        }
    }
}
