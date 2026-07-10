package com.mercadopago.sdk.android.example.presentation.features

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.example.R
import com.mercadopago.sdk.android.example.navigation.SampleDestination
import com.mercadopago.sdk.android.example.navigation.SampleFeature
import com.mercadopago.sdk.android.example.navigation.SampleFeaturesNavigationList
import com.mercadopago.sdk.android.example.presentation.components.AlertDialog
import com.mercadopago.sdk.android.example.presentation.theme.MercadoPagoSampleTheme
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK

@Composable
internal fun SampleFeaturesScreen(
    onFeatureClick: (SampleDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showSDKNotInitializedDialog by remember { mutableStateOf(false) }
    SampleSDKFeaturesList(
        onFeatureClick = { feature ->
            if (feature is SampleDestination.Home || feature is SampleDestination.SDKInstance) {
                onFeatureClick(feature)
            } else {
                if (MercadoPagoSDK.isInitialized) {
                    onFeatureClick(feature)
                } else {
                    showSDKNotInitializedDialog = true
                }
            }
        },
        modifier = modifier,
    )
    if (showSDKNotInitializedDialog) {
        AlertDialog(
            title = R.string.sdk_not_initialized_title,
            description = R.string.sdk_not_initialized_description,
            onDismissRequest = {
                showSDKNotInitializedDialog = false
            }
        )
    }
}


@Composable
internal fun SampleSDKFeaturesList(
    onFeatureClick: (SampleDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(SampleFeaturesNavigationList.filter { it.isEnabled }) { feature ->
            FeatureItem(
                feature = feature,
                onFeatureClick = onFeatureClick,
            )
            HorizontalDivider()
        }
    }
}

@Composable
internal fun FeatureItem(
    feature: SampleFeature,
    onFeatureClick: (SampleDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .testTag("home.${feature.destination::class.simpleName?.lowercase() ?: "feature"}")
            .clickable {
                onFeatureClick(feature.destination)
            }
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(id = feature.title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = stringResource(id = feature.description),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SampleSDKFeaturesListPreview() {
    MercadoPagoSampleTheme {
        SampleSDKFeaturesList(
            onFeatureClick = { },
        )
    }
}
