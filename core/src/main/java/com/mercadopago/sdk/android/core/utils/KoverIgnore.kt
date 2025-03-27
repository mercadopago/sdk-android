package com.mercadopago.sdk.android.core.utils

/**
 * Annotation to ignore code coverage in Kover.
 * @param reason The reason why the code is ignored.
 **/
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class KoverIgnore(@Suppress("unused") val reason: String)
