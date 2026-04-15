package com.mercadopago.sdk.android.coremethods.builder.domain.usecase

import com.mercadolibre.android.device.sdk.DeviceSDK
import com.mercadopago.sdk.android.coremethods.builder.data.local.mapper.toSiteId
import com.mercadopago.sdk.android.coremethods.builder.domain.model.MPDeviceSession
import com.mercadopago.sdk.android.coremethods.builder.domain.model.params.GetDeviceSessionParams
import com.mercadopago.sdk.android.coremethods.builder.domain.repository.MPExtendedRepository
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK

internal class GetDeviceSessionUseCase(
    private val repository: MPExtendedRepository,
) {
    suspend operator fun invoke(): Result<MPDeviceSession, ResultError> {
        return repository.getDeviceSession(
            GetDeviceSessionParams(
                device = DeviceSDK.getInstance()?.info,
                siteId = MercadoPagoSDK.countryCode?.toSiteId().orEmpty(),
            ),
        )
    }
}
