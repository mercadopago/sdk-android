# Gson reads @SerializedName at runtime; R8 strips annotations unless kept.
# Signature keeps generic types (e.g. Response<List<...>>), otherwise Gson returns LinkedTreeMap and the cast to
# the response DTO fails (ClassCastException) in the integrator's release build.
-keepattributes *Annotation*, Signature

# Public API only (explicit names): the domain models delivered to the integrator.
# Internal classes of these packages stay free to be obfuscated.
-keep public class com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayType { public *; }
-keep public class com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayType$* { public *; }
-keep public class com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayResult { public *; }
-keep public class com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayResult$* { public *; }
-keep public class com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayToken { public *; }
-keep public class com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayTransaction { public *; }
-keep public class com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayPaymentType { public *; }
-keep public class com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayError { public *; }
-keep public class com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayErrorType { public *; }
-keep public class com.mercadopago.sdk.android.googlepay.domain.model.MPGooglePayErrorCode { public *; }

# Request/response DTOs and Retrofit services are only reached via reflection (Gson/Retrofit),
# so R8 full mode would otherwise rename, merge or strip them in the integrator's release build.
-keep class com.mercadopago.sdk.android.googlepay.data.remote.request.** { *; }
-keep class com.mercadopago.sdk.android.googlepay.data.remote.response.** { *; }
-keep interface com.mercadopago.sdk.android.googlepay.data.remote.service.** { *; }
