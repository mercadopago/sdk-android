package com.mercadopago.sdk.android.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.telephony.TelephonyManager

private const val NETWORK_4G = 19

/**
 * Checks the current network type (Wi-Fi, 3G, 4G, 5G, or none).
 *
 * @param context The application context.
 * @return The current network type.
 */
@Suppress("ReturnCount")
@KoverIgnore("Network Behaviour")
fun checkNetworkType(context: Context): NetworkType {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val network = connectivityManager?.activeNetwork ?: return NetworkType.NONE
    val networkCapabilities =
        connectivityManager.getNetworkCapabilities(network) ?: return NetworkType.NONE

    return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
            NetworkType.WIFI
        }

        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
            when {
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) -> {
                    // Check the technology used for data connection.
                    val networkInfo = connectivityManager.getNetworkInfo(network)
                    when (networkInfo?.subtype) {
                        TelephonyManager.NETWORK_TYPE_GPRS,
                        TelephonyManager.NETWORK_TYPE_EDGE,
                        TelephonyManager.NETWORK_TYPE_CDMA,
                        TelephonyManager.NETWORK_TYPE_1xRTT,
                        TelephonyManager.NETWORK_TYPE_IDEN,
                        TelephonyManager.NETWORK_TYPE_GSM -> NetworkType.CELLULAR_UNKNOWN
                        TelephonyManager.NETWORK_TYPE_UMTS,
                        TelephonyManager.NETWORK_TYPE_EVDO_0,
                        TelephonyManager.NETWORK_TYPE_EVDO_A,
                        TelephonyManager.NETWORK_TYPE_HSDPA,
                        TelephonyManager.NETWORK_TYPE_HSUPA,
                        TelephonyManager.NETWORK_TYPE_HSPA,
                        TelephonyManager.NETWORK_TYPE_EVDO_B,
                        TelephonyManager.NETWORK_TYPE_EHRPD,
                        TelephonyManager.NETWORK_TYPE_HSPAP,
                        TelephonyManager.NETWORK_TYPE_TD_SCDMA -> NetworkType.CELLULAR_3G
                        TelephonyManager.NETWORK_TYPE_LTE,
                        TelephonyManager.NETWORK_TYPE_IWLAN,
                        NETWORK_4G -> NetworkType.CELLULAR_4G
                        TelephonyManager.NETWORK_TYPE_NR -> NetworkType.CELLULAR_5G
                        else -> NetworkType.CELLULAR_UNKNOWN
                    }
                }
                else -> NetworkType.CELLULAR_UNKNOWN
            }
        }
        else -> NetworkType.NONE
    }
}

/**
 * Enum class representing different network types.
 *
 * Each network type is associated with a string representation that can be used for
 * display or identification purposes.
 *
 * @property text The string representation of the network type.
 */
@KoverIgnore("Network Behaviour")
enum class NetworkType(val text: String) {
    /**
     *  Wifi connection type.
     */
    WIFI("wifi"),
    /**
     *  Cellular connection type of 3G.
     */
    CELLULAR_3G("cellular_3g"),
    /**
     *  Cellular connection type of 4G.
     */
    CELLULAR_4G("cellular_4g"),
    /**
     *  Cellular connection type of 5G.
     */
    CELLULAR_5G("cellular_5g"),
    /**
     *  Unknown cellular connection type.
     */
    CELLULAR_UNKNOWN("cellular_unknown"),
    /**
     * No network type detected
     */
    NONE("none")
}
