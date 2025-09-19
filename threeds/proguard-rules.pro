# ===================================
# MercadoPago 3DS SDK ProGuard Rules
# ===================================
# These rules provide security and obfuscation for the 3DS module
# while preserving necessary APIs and third-party library requirements

# ===================================
# DEBUGGING CONFIGURATION
# ===================================
# Preserve line numbers for debugging (can be disabled in production)
-keepattributes SourceFile,LineNumberTable
# Rename source files to hide original names
-renamesourcefileattribute SourceFile

# ===================================
# GENERAL ANDROID OPTIMIZATIONS
# ===================================
# Enable aggressive optimizations
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# ===================================
# PUBLIC API PRESERVATION
# ===================================
# Keep main 3DS SDK entry point and its public methods
-keep class com.mercadopago.sdk.android.threeds.interactor.MPThreeDS {
    public *;
    # Keep companion object methods for getInstance()
    public static ** getInstance();
}

# Keep all public domain models that are part of the API
-keep class com.mercadopago.sdk.android.threeds.domain.model.** {
    public *;
}

# Keep public parameter classes
-keep class com.mercadopago.sdk.android.threeds.domain.model.params.** {
    public *;
}

# ===================================
# KOTLIN SPECIFIC RULES
# ===================================
# Preserve Kotlin metadata for proper reflection
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep Kotlin coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Keep Kotlin companion objects
-keep class **$Companion {
    *;
}

# ===================================
# GSON SERIALIZATION RULES
# ===================================
# Keep Gson classes and prevent obfuscation of serialized fields
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }

# Keep all model classes that might be serialized
-keep class com.mercadopago.sdk.android.threeds.data.model.** {
    <fields>;
    <init>(...);
}

# ===================================
# uSDK (Universal SDK) PRESERVATION
# ===================================
# Keep uSDK classes and interfaces - critical for 3DS functionality
-keep class com.usdk.** { *; }
-keep interface com.usdk.** { *; }

# Keep EMVCo 3DS Core classes - required by 3DS specification
-keep class org.emvco.threeds.** { *; }
-keep interface org.emvco.threeds.** { *; }

# Preserve callback interfaces and their methods
-keep class * implements org.emvco.threeds.core.ChallengeStatusReceiver {
    public *;
}

# ===================================
# ANDROID COMPONENTS PRESERVATION
# ===================================
# Keep BroadcastReceiver implementations
-keep class * extends android.content.BroadcastReceiver {
    public *;
}

# Keep Activity classes that might be used by 3DS challenges
-keep class * extends android.app.Activity {
    public *;
}

# ===================================
# DEPENDENCY INJECTION (KOIN) RULES
# ===================================
# Keep Koin module providers and their methods
-keep class com.mercadopago.sdk.android.threeds.di.** {
    public *;
}

# Keep classes that are injected by Koin
-keep class * {
    @org.koin.core.annotation.* *;
}

# ===================================
# REFLECTION PREVENTION
# ===================================
# Prevent reflection access to sensitive internal classes
-keepclassmembers class com.mercadopago.sdk.android.threeds.data.wrapper.ThreeDSWrapper {
    !public *;
}

-keepclassmembers class com.mercadopago.sdk.android.threeds.data.repository.** {
    !public *;
}

# ===================================
# SECURITY HARDENING
# ===================================
# Remove logging in release builds (security measure)
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Remove debug information from stack traces
-keepattributes !LocalVariableTable,!LocalVariableTypeTable

# Obfuscate class names but preserve critical functionality
-keep,allowobfuscation class com.mercadopago.sdk.android.threeds.data.** {
    !public *;
}

# ===================================
# ERROR HANDLING PRESERVATION
# ===================================
# Keep exception classes for proper error handling
-keep class * extends java.lang.Exception {
    public *;
}

-keep class * extends java.lang.Throwable {
    public *;
}

# ===================================
# NATIVE METHODS PRESERVATION
# ===================================
# Keep native methods (in case uSDK uses JNI)
-keepclasseswithmembernames class * {
    native <methods>;
}

# ===================================
# ENUM PRESERVATION
# ===================================
# Keep enum classes and their values method
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ===================================
# PARCELABLE PRESERVATION
# ===================================
# Keep Parcelable implementations (if any)
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ===================================
# FINAL WARNINGS SUPPRESSION
# ===================================
# Suppress warnings for missing classes that might not be used
-dontwarn com.usdk.**
-dontwarn org.emvco.threeds.**
-dontwarn com.google.android.gms.**

# ===================================
# OPTIMIZATION EXCLUSIONS
# ===================================
# Don't optimize certain critical security classes
-keep,allowshrinking,allowobfuscation class com.mercadopago.sdk.android.threeds.data.wrapper.ThreeDSWrapper
-keep,allowshrinking,allowobfuscation class com.mercadopago.sdk.android.threeds.data.repository.ThreeDSRepositoryImpl
