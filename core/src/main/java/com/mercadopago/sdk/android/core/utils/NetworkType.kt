package com.mercadopago.sdk.android.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.telephony.TelephonyManager

private const val NETWORK_4G = 19

/**
 * Represents the different types of network connections available on the device.
 * This enum provides a standardized way to identify and handle various network types,
 * including Wi-Fi and different generations of cellular networks.
 * It is used throughout the SDK to optimize network operations based on connection type.
 *
 * @param text the network type name
 *
 * Example:
 * ```kotlin
 * // Check current network type
 * val networkType = checkNetworkType(context)
 *
 * // Handle different network types
 * when (networkType) {
 *     NetworkType.WIFI -> {
 *         // Handle Wi-Fi connection
 *     }
 *     NetworkType.CELLULAR_4G, NetworkType.CELLULAR_5G -> {
 *         // Handle high-speed cellular connection
 *     }
 *     NetworkType.NONE -> {
 *         // Handle no connection
 *     }
 *     else -> {
 *         // Handle other connection types
 *     }
 * }
 * ```
 *
 * @see checkNetworkType
 */
@KoverIgnore("Network Behaviour")
enum class NetworkType(val text: String) {
    /**
     * Represents a Wi-Fi network connection.
     * This type indicates that the device is connected to a wireless network,
     * typically providing high-speed internet access.
     *
     * Example:
     * ```kotlin
     * if (networkType == NetworkType.WIFI) {
     *     // Enable high-bandwidth features
     * }
     * ```
     */
    WIFI("wifi"),

    /**
     * Represents a 3G cellular network connection.
     * This type indicates that the device is connected to a third-generation
     * mobile network, providing moderate-speed data transfer.
     *
     * Example:
     * ```kotlin
     * if (networkType == NetworkType.CELLULAR_3G) {
     *     // Enable moderate-bandwidth features
     * }
     * ```
     */
    CELLULAR_3G("cellular_3g"),

    /**
     * Represents a 4G cellular network connection.
     * This type indicates that the device is connected to a fourth-generation
     * mobile network, providing high-speed data transfer.
     *
     * Example:
     * ```kotlin
     * if (networkType == NetworkType.CELLULAR_4G) {
     *     // Enable high-bandwidth features
     * }
     * ```
     */
    CELLULAR_4G("cellular_4g"),

    /**
     * Represents a 5G cellular network connection.
     * This type indicates that the device is connected to a fifth-generation
     * mobile network, providing ultra-high-speed data transfer.
     *
     * Example:
     * ```kotlin
     * if (networkType == NetworkType.CELLULAR_5G) {
     *     // Enable ultra-high-bandwidth features
     * }
     * ```
     */
    CELLULAR_5G("cellular_5g"),

    /**
     * Represents an unknown or undetermined cellular network connection.
     * This type is used when the device is connected to a cellular network,
     * but the specific generation cannot be determined.
     *
     * Example:
     * ```kotlin
     * if (networkType == NetworkType.CELLULAR_UNKNOWN) {
     *     // Use conservative bandwidth settings
     * }
     * ```
     */
    CELLULAR_UNKNOWN("cellular_unknown"),

    /**
     * Represents no network connection.
     * This type indicates that the device is not connected to any network,
     * either Wi-Fi or cellular.
     *
     * Example:
     * ```kotlin
     * if (networkType == NetworkType.NONE) {
     *     // Show offline mode or connection error
     * }
     * ```
     */
    NONE("none")
}

/**
 * Determines the current network connection type of the device.
 * This function checks the active network connection and returns the corresponding
 * NetworkType enum value. It handles various network technologies including
 * Wi-Fi, 3G, 4G, 5G, and other cellular networks.
 *
 * @param context The application context used to access system services
 * @return The current NetworkType representing the active connection
 *
 * Example:
 * ```kotlin
 * // Check network type in an activity or fragment
 * val networkType = checkNetworkType(context)
 *
 * // Use the result to adjust app behavior
 * when (networkType) {
 *     NetworkType.WIFI -> {
 *         // Enable high-bandwidth features
 *     }
 *     NetworkType.CELLULAR_4G, NetworkType.CELLULAR_5G -> {
 *         // Enable high-bandwidth features with caution
 *     }
 *     NetworkType.CELLULAR_3G -> {
 *         // Enable moderate-bandwidth features
 *     }
 *     NetworkType.NONE -> {
 *         // Show offline mode
 *     }
 *     else -> {
 *         // Use conservative settings
 *     }
 * }
 * ```
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
