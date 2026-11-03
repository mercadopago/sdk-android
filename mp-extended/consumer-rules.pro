-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Gson reads @SerializedName at runtime (e.g. "finger_print", "site_id"); R8 strips annotations unless kept.
# Signature keeps the generic type of Response<DeviceSessionIdResponse>, otherwise Gson returns a
# LinkedTreeMap and the cast to the response DTO fails (ClassCastException) in release builds.
-keepattributes *Annotation*, Signature

# Request/response DTOs and the Retrofit service are only reached via reflection (Gson/Retrofit),
# so R8 full mode would otherwise rename, merge or strip them in the integrator's release build.
-keep class com.mercadopago.sdk.android.mpextended.data.remote.request.** { *; }
-keep class com.mercadopago.sdk.android.mpextended.data.remote.response.** { *; }
-keep interface com.mercadopago.sdk.android.mpextended.data.remote.service.** { *; }

# Device SDK fingerprint data model is serialized by Gson in MPDeviceSessionIdRequest ("finger_print").
# Fingerprint nests other domain types (VendorSpecificAttributes, VendorId), so keep the whole domain package
# to stop R8 from renaming classes and fields that end up in the JSON payload.
-keep class com.mercadolibre.android.device.sdk.domain.** { *; }
