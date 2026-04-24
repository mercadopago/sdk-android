# Add project specific ProGuard rules here.
-dontwarn java.lang.invoke.StringConcatFactory
# Keep all public classes
-keep public class ** {
    public *;
}
-keepclassmembers public class ** {
    public *;
}
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class * {
    @com.google.gson.annotations.SerializedName *;
}
-keepnames class com.mercadopago.sdk.android.mpextended.** { *; }
-keep class com.mercadopago.sdk.android.mpextended.** { *; }
