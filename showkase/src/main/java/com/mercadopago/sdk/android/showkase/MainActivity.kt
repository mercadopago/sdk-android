package com.mercadopago.sdk.android.showkase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.airbnb.android.showkase.models.Showkase

internal class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startActivity(Showkase.getBrowserIntent(this))
        finish()
    }
}
