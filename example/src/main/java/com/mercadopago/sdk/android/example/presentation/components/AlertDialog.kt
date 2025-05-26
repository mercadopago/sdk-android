package com.mercadopago.sdk.android.example.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.example.R
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AlertDialog(
    @StringRes title: Int,
    @StringRes description: Int?,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    @StringRes confirmButtonTitle: Int = R.string.ok,
    content: @Composable () -> Unit = { },
) {
    AlertDialog(
        title = stringResource(title),
        description = description?.let { stringResource(it) },
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        confirmButtonTitle = stringResource(confirmButtonTitle),
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AlertDialog(
    title: String,
    description: String?,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    confirmButtonTitle: String = stringResource(R.string.ok),
    content: @Composable () -> Unit = { },
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
            )
            description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
            Button(
                onClick = onDismissRequest,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(
                    text = confirmButtonTitle,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Preview
@Composable
fun AlertDialogPreview() {
    MercadoPagoSampleTheme {
        AlertDialog(
            title = R.string.app_name,
            description = R.string.app_name,
            onDismissRequest = { },
        )
    }
}
