package com.mercadopago.sdk.android.coremethods

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(name = "Greeting", group = "Text")
@Composable
fun GreetingPreview() {
    MaterialTheme {
        Greeting("Android")
    }
}
