package com.mercadopago.sdk.android.example.presentation.sdkinitializer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.example.R
import com.mercadopago.sdk.android.example.domain.model.PublicKey
import com.mercadopago.sdk.android.example.presentation.components.AlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SdkInitializerScreen(
    modifier: Modifier = Modifier,
    viewModel: SdkInitializerViewModel = viewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()

    SdkInitializerScreen(
        viewState = viewState,
        onPublicKeyChange = viewModel::onPublicKeyChange,
        onCountryCodeSelected = viewModel::onCountryCodeSelected,
        onInitializeSdkClick = viewModel::onInitializeSdkClick,
        onDestroySdkInstanceClick = viewModel::onDestroySdkInstanceClick,
        onDialogStateChanged = viewModel::onDialogStateChanged,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SdkInitializerScreen(
    viewState: SdkInitializerViewState,
    onPublicKeyChange: (String) -> Unit,
    onCountryCodeSelected: (CountryCode) -> Unit,
    onInitializeSdkClick: () -> Unit,
    onDestroySdkInstanceClick: () -> Unit,
    onDialogStateChanged: (SdkInitializerDialogState) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (viewState.dialogState) {
        SdkInitializerDialogState.EmptyPublicKey -> AlertDialog(
            title = R.string.empty_public_key_error_title,
            description = R.string.empty_public_key_error_description,
            onDismissRequest = { onDialogStateChanged(SdkInitializerDialogState.Hidden) },
        )
        is SdkInitializerDialogState.Autofill -> AlertDialog(
            title = R.string.autofill_public_key,
            description = null,
            onDismissRequest = { onDialogStateChanged(SdkInitializerDialogState.Hidden) },
        ) {
            AutofillPublicKeyList(
                list = viewState.publicKeyList,
                onClick = {
                    onPublicKeyChange(it.publicKey)
                    onCountryCodeSelected(CountryCode.valueOf(it.countryCode))
                    onDialogStateChanged(SdkInitializerDialogState.Hidden)
                },
            )
        }
        SdkInitializerDialogState.Hidden -> Unit
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var countryCodeDropdownExpanded by remember { mutableStateOf(false) }
        val statusColor = if (viewState.sdkState.isInitialized) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        }
        Text("SDK Status: ${if (viewState.sdkState.isInitialized) "Initialized" else "Not Initialized"}", color = statusColor)

        if (viewState.sdkState.isInitialized) {
            Text("Current Public Key: ${viewState.sdkState.publicKey}")
            Text("Current Country Code: ${viewState.sdkState.countryCode}")
        }

        if (viewState.publicKeyList.isNotEmpty()) {
            Button(
                onClick = { onDialogStateChanged(SdkInitializerDialogState.Autofill) }
            ) {
                Text(
                    text = stringResource(R.string.autofill_public_key)
                )
            }
        }

        OutlinedTextField(
            value = viewState.publicKeyInput,
            onValueChange = onPublicKeyChange,
            label = { Text("Public Key") },
            modifier = Modifier.fillMaxWidth().testTag("sdk.publicKey")
        )

        ExposedDropdownMenuBox(
            expanded = countryCodeDropdownExpanded,
            onExpandedChange = { countryCodeDropdownExpanded = !countryCodeDropdownExpanded },
            modifier = Modifier.fillMaxWidth().testTag("sdk.countryCode")
        ) {
            OutlinedTextField(
                value = viewState.selectedCountryCode.toString(),
                onValueChange = {},
                readOnly = true,
                label = { Text("Country Code") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryCodeDropdownExpanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = countryCodeDropdownExpanded,
                onDismissRequest = { countryCodeDropdownExpanded = false }
            ) {
                viewState.countryCodeOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.toString()) },
                        onClick = {
                            onCountryCodeSelected(CountryCode.valueOf(selectionOption.toString()))
                            countryCodeDropdownExpanded = false
                        },
                        modifier = Modifier.testTag("sdk.country.${selectionOption.name.lowercase()}")
                    )
                }
            }
        }

        Button(
            onClick = onInitializeSdkClick,
            modifier = Modifier.fillMaxWidth().testTag("sdk.initializeSdk"),
            enabled = true,
        ) {
            Text("Change SDK Configuration")
        }
    }
}

@Composable
private fun AutofillPublicKeyList(
    list: List<PublicKey>,
    onClick: (PublicKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn {
        items(list) { publicKey ->
            AutofillPublicKeyItem(
                publicKey = publicKey,
                onClick = onClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun AutofillPublicKeyItem(
    publicKey: PublicKey,
    onClick: (PublicKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = false,
                onClick = { onClick(publicKey) }
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Public Key: " + publicKey.publicKey,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = "Country Code: " + publicKey.countryCode,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        RadioButton(
            selected = false,
            onClick = { onClick(publicKey) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SdkInitializerScreenSDKInitializedPreview() {
    SdkInitializerScreen(
        viewState = SdkInitializerViewState(
            sdkState = SdkState(
                isInitialized = true,
                publicKey = "test-public-key",
                countryCode = CountryCode.ARG,
            )
        ),
        onPublicKeyChange = { },
        onCountryCodeSelected = { },
        onInitializeSdkClick = { },
        onDestroySdkInstanceClick = { },
        onDialogStateChanged = { },
    )
}

@Preview(showBackground = true)
@Composable
fun SdkInitializerScreenSDKNotInitializedPreview() {
    SdkInitializerScreen(
        viewState = SdkInitializerViewState(),
        onPublicKeyChange = { },
        onCountryCodeSelected = { },
        onInitializeSdkClick = { },
        onDestroySdkInstanceClick = { },
        onDialogStateChanged = { },
    )
}
