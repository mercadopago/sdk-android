package com.mercadopago.sdk.android.di

import org.koin.core.module.Module
import org.koin.dsl.module

internal fun List<Module>.toModule(): Module = module {
    includes(this@toModule)
}
