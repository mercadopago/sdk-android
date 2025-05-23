package com.mercadopago.sdk.android.example.navigation

import androidx.annotation.StringRes
import com.mercadopago.sdk.android.example.R

internal val SampleFeaturesNavigationList: List<SampleFeature> = listOf(
    SampleFeature(
        title = R.string.sdk_instance_feature_title,
        description = R.string.sdk_instance_feature_description,
        destination = SampleDestination.SDKInstance,
    ),
    SampleFeature(
        title = R.string.core_methods_feature_title,
        description = R.string.core_methods_feature_description,
        destination = SampleDestination.CoreMethods,
    )
)

internal data class SampleFeature(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val destination: SampleDestination,
)
