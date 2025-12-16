# =============================================
# MercadoPago 3DS SDK Consumer ProGuard Rules
# =============================================
# These rules are automatically applied when this library is consumed
# by other projects. They ensure the public API remains accessible
# while protecting internal implementation details.

# =============================================
# PUBLIC API PRESERVATION
# =============================================
# Keep the main 3DS SDK entry point - this is the primary public API
-keep class com.mercadopago.sdk.android.threeds.interactor.MPThreeDS {
    public *;
    # Specifically keep the getInstance method for singleton access
    public static com.mercadopago.sdk.android.threeds.interactor.MPThreeDS getInstance();
}

# =============================================
# PUBLIC DOMAIN MODELS
# =============================================
# Keep all public domain model classes that clients interact with
-keep class com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel {
    public *;
    <init>(...);
}

-keep class com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated {
    public *;
    <init>(...);
}

-keep class com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError {
    public *;
    <init>(...);
}

-keep class com.mercadopago.sdk.android.threeds.domain.model.MpThreeDSChallengeResult {
    public *;
    <init>(...);
}

-keep class com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning {
    public *;
    <init>(...);
}

# Keep enums and their methods
-keep enum com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSSeverity {
    public *;
}

# =============================================
# PARAMETER CLASSES
# =============================================
# Keep parameter classes used in public API
-keep class com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams {
    public *;
    <init>(...);
}

# =============================================
# RESULT SEALED CLASSES
# =============================================
# Keep sealed class hierarchy for challenge results
-keep class com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult {
    public *;
}

-keep class com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult$* {
    public *;
    <init>(...);
}

# =============================================
# KOTLIN COMPATIBILITY
# =============================================
# Preserve Kotlin metadata for public API classes
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep Kotlin companion objects for public classes
-keep class com.mercadopago.sdk.android.threeds.interactor.MPThreeDS$Companion {
    public *;
}

# =============================================
# SERIALIZATION SUPPORT
# =============================================
# Keep fields for potential JSON serialization of public models
-keepclassmembers class com.mercadopago.sdk.android.threeds.domain.model.** {
    <fields>;
}

# =============================================
# COROUTINES SUPPORT
# =============================================
# Ensure coroutine-based methods work correctly
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# =============================================
# THIRD-PARTY DEPENDENCIES
# =============================================
# Ensure uSDK classes remain accessible for 3DS functionality
-keep class com.usdk.** { *; }
-keep class org.emvco.threeds.** { *; }

# =============================================
# ANDROID COMPONENTS
# =============================================
# Keep Activity parameter types for doChallenge method
-keep class android.app.Activity {
    public *;
}

# =============================================
# EXCEPTION HANDLING
# =============================================
# Keep exception classes that might be thrown by public API
-keep class * extends java.lang.Exception {
    public <init>(java.lang.String);
    public <init>(java.lang.String, java.lang.Throwable);
}

# =============================================
# ENUM PRESERVATION
# =============================================
# Ensure enum methods work correctly in consuming applications
-keepclassmembers enum com.mercadopago.sdk.android.threeds.domain.model.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# =============================================
# WARNING SUPPRESSIONS
# =============================================
# Suppress warnings for classes that may not be present in consuming app
-dontwarn com.mercadopago.sdk.android.threeds.data.**
-dontwarn com.mercadopago.sdk.android.threeds.di.**
