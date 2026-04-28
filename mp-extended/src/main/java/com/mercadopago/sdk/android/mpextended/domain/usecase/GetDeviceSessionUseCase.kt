package com.mercadopago.sdk.android.mpextended.domain.usecase

import com.mercadolibre.android.device.sdk.DeviceSDK
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import com.mercadopago.sdk.android.mpextended.domain.model.MPDeviceSession
import com.mercadopago.sdk.android.mpextended.domain.model.params.GetDeviceSessionParams
import com.mercadopago.sdk.android.mpextended.domain.repository.MPExtendedRepository

internal class GetDeviceSessionUseCase(
    private val repository: MPExtendedRepository,
) {
    suspend operator fun invoke(): Result<MPDeviceSession, ResultError> {
        return repository.getDeviceSession(
            GetDeviceSessionParams(
                device = DeviceSDK.getInstance()?.info,
                siteId = MercadoPagoSDK.getSiteId(),
            ),
        )
    }
}
