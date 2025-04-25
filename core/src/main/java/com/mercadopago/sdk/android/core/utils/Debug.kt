package com.mercadopago.sdk.android.core.utils

import android.content.Context
import android.content.pm.ApplicationInfo

private const val LIBRARY_GROUP = "com.mercadopago.sdk.android"

/**
* Use this method to check if the integrator's app is in debug mode.
**/
fun isDebugApp(context: Context): Boolean =
    context.applicationInfo?.flags?.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0

/**
 * Checks if the integrator's application belongs to the same library group as the current library.
 *
 * @param context The application context.
 * @return `true` if the application's package name contains `LIBRARY_GROUP`, `false` otherwise.
 */
@KoverIgnore("System Behaviour")
fun isSameLibraryGroup(context: Context): Boolean =
    context.applicationInfo?.packageName?.contains(LIBRARY_GROUP) == true
