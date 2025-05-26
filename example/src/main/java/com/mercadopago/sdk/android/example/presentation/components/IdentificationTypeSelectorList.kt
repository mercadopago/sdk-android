package com.mercadopago.sdk.android.example.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.example.extensions.addBorder
import com.mercadopago.sdk.android.example.presentation.coremethods.state.IdentificationState
import com.mercadopago.sdk.android.example.presentation.components.extensions.getPlaceholder
import com.mercadopago.sdk.android.example.presentation.components.extensions.getVisualTransformation

@Suppress("LongMethod")
@Composable
internal fun IdentificationTypeSelectorField(
    modifier: Modifier = Modifier,
    state: IdentificationState,
    onSelectIdentification: (IdentificationType) -> Unit,
    onIdentificationTypeChanged: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Label(
            text = "Cardholder ID",
        )
        Spacer(Modifier.height(4.dp))
        BasicTextField(
            value = state.identificationValue,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            onValueChange = { value ->
                if (value.length <= (state.selectedIdentification?.maxLength ?: 0)) {
                    onIdentificationTypeChanged(value)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (state.selectedIdentification?.type == "number") {
                    KeyboardType.Number
                } else {
                    KeyboardType.Unspecified
                },
            ),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(OutlinedTextFieldDefaults.MinHeight)
                        .addBorder(isFocused)
                        .padding(horizontal = 16.dp),
                ) {
                    IdentificationTypeSelectorList(
                        state = state,
                        onSelectIdentification = { identificationType ->
                            onSelectIdentification(identificationType)
                            onIdentificationTypeChanged("")
                        },
                    )
                    VerticalDivider(modifier = Modifier.height(40.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(Modifier.fillMaxWidth()) {
                        if (state.identificationValue.isEmpty()) {
                            PlaceHolder(
                                text = state.selectedIdentification.getPlaceholder(),
                                modifier = Modifier.align(Alignment.CenterStart),
                            )
                        }
                        innerTextField()
                    }
                }
            },
            visualTransformation = state.selectedIdentification.getVisualTransformation(),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun IdentificationTypeSelectorList(
    modifier: Modifier = Modifier,
    state: IdentificationState,
    onSelectIdentification: (IdentificationType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
        ) {
            Label(
                text = state.selectedIdentification?.name.orEmpty(),
                modifier = modifier.widthIn(min = 32.dp)
            )
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            state.identificationList.forEach { option ->
                DropdownMenuItem(
                    text = {
                        option.name?.let {
                            Label(text = it)
                        }
                    },
                    onClick = {
                        expanded = false
                        onSelectIdentification(option)
                    }
                )
            }
        }
    }
}
