package com.mercadopago.sdk.android.example.di

import com.mercadopago.sdk.android.example.MainApplication
import com.mercadopago.sdk.android.example.presentation.coremethods.PaymentScreenViewModel
import com.mercadopago.sdk.android.example.presentation.logs.LogsViewModel
import com.mercadopago.sdk.android.example.presentation.mpextended.MPExtendedViewModel
import com.mercadopago.sdk.android.example.presentation.sdkinitializer.SdkInitializerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal fun providePresentationModule(application: MainApplication) = module {
    viewModel { PaymentScreenViewModel() }
    viewModel {
        SdkInitializerViewModel(
            application = application,
        )
    }
    viewModel { LogsViewModel() }
    viewModel { MPExtendedViewModel() }
}
