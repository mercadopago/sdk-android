package com.mercadopago.sdk.android.analytics.data.datasource.local

import com.mercadopago.sdk.android.analytics.data.local.model.SessionId
import kotlinx.coroutines.flow.Flow

internal interface AnalyticsLocalDataSource {

    fun getSessionId(): Flow<SessionId>

    fun getUid(): Flow<String>

    fun setSessionId(sessionId: SessionId): Flow<Unit>

    fun setUid(uid: String): Flow<Unit>
}
