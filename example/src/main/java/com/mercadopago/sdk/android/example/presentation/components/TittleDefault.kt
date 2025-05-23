package com.mercadopago.sdk.android.example.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Title(
    text: String = "",
    textStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(
        fontWeight = FontWeight.SemiBold,
    ),
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    TitleComposable(
        text = text,
        style = textStyle,
        color = textColor,
        modifier = modifier
    )
}

@Composable
fun TitleComposable(
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
