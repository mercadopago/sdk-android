package com.mercadopago.sdk.android.core.utils

import android.content.Context
import com.mercadopago.sdk.android.core.BuildConfig

private const val LIBRARY_GROUP = "com.mercadopago.sdk.android"

/**
* Use this method to check if the app is in debug mode.
**/
fun isDebugApp(): Boolean =
    BuildConfig.DEBUG

/**
 * Checks if the application belongs to the same library group as the current library.
 *
 * @param context The application context.
 * @return `true` if the application's package name contains `LIBRARY_GROUP`, `false` otherwise.
 */
fun isSameLibraryGroup(context: Context): Boolean =
    context.applicationInfo.packageName.contains(LIBRARY_GROUP)
