package com.mercadopago.sdk.android.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.example.presentation.state.IdentificationState

@Composable
internal fun IdentificationName(
    modifier: Modifier = Modifier,
    identificationState: IdentificationState,
    onCardHolderNameChanged: (String) -> Unit
) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(8.dp))
        Label(text = "Cardholder's name as it appears on the card")
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = identificationState.identificationNameValue,
            placeholder = {
                PlaceHolder(text = "María López")
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            onValueChange = onCardHolderNameChanged,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
