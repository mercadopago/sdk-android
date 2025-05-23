package com.mercadopago.sdk.android.core.sample

import androidx.annotation.RestrictTo

/**
 * Sampled annotation is used to mark a method as sampled.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Sampled
