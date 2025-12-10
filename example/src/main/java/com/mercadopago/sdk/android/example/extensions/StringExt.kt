package com.mercadopago.sdk.android.example.extensions

fun String.formatPublicKey() : String =  this.trim('"').replace("\\\"", "\"")
