package com.mercadopago.sdk.android.example.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun PlaceHolder(
    text: String = "",
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier: Modifier = Modifier
) {
    PlaceHolderComposable(
        text = text,
        style = textStyle,
        color = textColor,
        modifier = modifier
    )
}

@Composable
fun PlaceHolderComposable(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier
) {
    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier
    )
}
