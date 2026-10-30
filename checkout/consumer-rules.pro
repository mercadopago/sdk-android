-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Gson reads @SerializedName at runtime; R8 strips annotations unless kept.
# Signature keeps generic types (e.g. Response<T>), otherwise Gson returns LinkedTreeMap and the cast to the
# response DTO fails (ClassCastException) in the integrator's release build.
-keepattributes *Annotation*, Signature

# BFF request/response DTOs, Retrofit services, analytics event data and Gson-mapped domain models are only
# reached via reflection (Gson/Retrofit), so R8 full mode would otherwise rename, merge or strip them in the
# integrator's release build.
-keep class com.mercadopago.sdk.android.checkout.data.remote.request.** { *; }
-keep class com.mercadopago.sdk.android.checkout.data.remote.response.** { *; }
-keep interface com.mercadopago.sdk.android.checkout.data.remote.service.** { *; }
-keep class com.mercadopago.sdk.android.checkout.statusscreen.data.remote.request.** { *; }
-keep class com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.** { *; }
-keep interface com.mercadopago.sdk.android.checkout.statusscreen.data.remote.service.** { *; }
-keep class com.mercadopago.sdk.android.checkout.analytics.** { *; }
-keep class com.mercadopago.sdk.android.checkout.domain.model.** { *; }
