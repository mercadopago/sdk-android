# Gson reads @SerializedName at runtime; R8 strips annotations unless kept.
# Signature keeps generic types (e.g. Response<List<...>>), otherwise Gson returns LinkedTreeMap and the cast to
# the response DTO fails (ClassCastException) in the integrator's release build.
-keepattributes *Annotation*, Signature

# Request/response DTOs, Retrofit services and analytics event data are only reached via reflection
# (Gson/Retrofit), so R8 full mode would otherwise rename, merge or strip them in the integrator's release build.
-keep class com.mercadopago.sdk.android.coremethods.data.remote.request.** { *; }
-keep class com.mercadopago.sdk.android.coremethods.data.remote.response.** { *; }
-keep interface com.mercadopago.sdk.android.coremethods.data.remote.service.** { *; }
-keep class com.mercadopago.sdk.android.coremethods.analytics.** { *; }

# ResultError.Request is parsed by Gson from the error body by FIELD NAME (no @SerializedName) in core-methods and
# mp-extended ResponseMapper. If R8 renames "message"/"code", Gson leaves them null and the non-null Kotlin
# parameters crash downstream (NPE in the payment methods / installments error analytics).
-keep class com.mercadopago.sdk.android.coremethods.domain.model.ResultError { *; }
-keep class com.mercadopago.sdk.android.coremethods.domain.model.ResultError$* { *; }

# Device SDK data model is serialized by Gson in CardTokenBodyRequest ("device"): Device -> Fingerprint ->
# VendorSpecificAttributes / VendorId. Keep the whole domain package so integrators that use core-methods
# (or checkout) without mp-extended also send the fingerprint with its original field names.
-keep class com.mercadolibre.android.device.sdk.domain.** { *; }
