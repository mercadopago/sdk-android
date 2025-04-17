package com.mercadopago.sdk.android.analytics.domain.interactor

import android.content.Context
import android.util.Log
import com.mercadopago.sdk.android.analytics.di.AnalyticsModulesProvider
import com.mercadopago.sdk.android.analytics.domain.exception.AnalyticsInitializationException
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.usecase.TrackMetricUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.Koin

/** Core analytics implementation for the MercadoPago SDK.
 *
 * This class is responsible for:
 * - Collecting events and screen views
 * - Aggregating environment data
 * - Formatting and sending analytics data
 *
 *  This class have to be initialized first with [initialize] method
 *  then get a instance by [getInstance]
 * */
class MPAnalytics internal constructor(
    private val koin: Koin,
) {

    /**
     * Companion object for the [MPAnalytics] class.
     */
    companion object {
        @Volatile
        private var instance: MPAnalytics? = null

        /**
         * Gets the instance of the [MPAnalytics] class.
         */
        fun getInstance(): MPAnalytics {
            return instance ?: throw AnalyticsInitializationException()
        }

        /** Call this method to initialize the analytics instance.
         * @param context application context.
         * @param getSiteIdFlow a flow that emits the current siteId.
         * */
        fun initialize(
            context: Context,
            getSiteIdFlow: Flow<String>,
        ) {
            val modulesProvider = AnalyticsModulesProvider(
                getSiteIdFlow = getSiteIdFlow,
                context = context,
            )
            instance = MPAnalytics(
                koin = modulesProvider.koinApp,
            )
        }
    }

    /** Processes and sends the current event
     * This method:
     * 1. Collects user information
     * 2. Builds the payload with all required data
     * 4. Sends the data (currently just prints to console)
     */
    fun trackMetric(metric: Metric) {
        val trackMetricUseCase = koin.get<TrackMetricUseCase>()
        CoroutineScope(Dispatchers.IO).launch {
            trackMetricUseCase(metric)
                .catch {
                    Log.e("MPAnalytics", "Error while tracking metric", it)
                }.firstOrNull()
        }
    }
}
