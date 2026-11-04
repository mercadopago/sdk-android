package com.mercadopago.sdk.android.checkout.presentation.shared

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import java.util.WeakHashMap

@Composable
internal fun SecureWhileVisible() {
    val activity = LocalContext.current.findActivity() ?: return
    DisposableEffect(activity) {
        SecureWindowOwners.acquire(activity.window)
        onDispose {
            SecureWindowOwners.release(activity.window)
        }
    }
}

internal object SecureWindowOwners {
    private val activeCount = WeakHashMap<Window, Int>()
    private val hostHadFlag = WeakHashMap<Window, Boolean>()

    fun acquire(
        window: Window,
    ) {
        val count = activeCount[window] ?: 0
        if (count == 0) {
            hostHadFlag[window] = window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        activeCount[window] = count + 1
    }

    fun release(
        window: Window,
    ) {
        val count = (activeCount[window] ?: return) - 1
        if (count > 0) {
            activeCount[window] = count
            return
        }
        activeCount.remove(window)
        if (hostHadFlag.remove(window) != true) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
