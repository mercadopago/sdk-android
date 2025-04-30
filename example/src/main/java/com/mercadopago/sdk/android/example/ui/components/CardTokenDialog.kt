package com.mercadopago.sdk.android.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.example.ui.theme.ExampleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardTokenDialog(
    token: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
        ) {
            Spacer(Modifier.height(10.dp))
            Title(
                text = "Token Generated",
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(Modifier.height(20.dp))
            Label(text = "The card token is: $token")
            Button(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.End),
            ) {
                Label(text = "Ok")
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Preview
@Composable
fun CardTokenDialogPreview() {
    ExampleTheme {
        CardTokenDialog(
            token = "1234",
            onDismiss = { },
        )
    }
}
