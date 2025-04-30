package com.mercadopago.sdk.android.example.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun Label(
    text: String = "",
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier
) {
    LabelComposable(
        text = text,
        style = textStyle,
        color = textColor,
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun LabelComposable(
    text: String,
    style: TextStyle,
    color: Color,
    maxLines: Int,
    modifier: Modifier
) {
    Text(
        text = text,
        style = style,
        color = color,
        maxLines = maxLines,
        modifier = modifier
    )
}
