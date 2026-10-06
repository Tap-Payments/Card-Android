-keepclassmembers class * {
    static java.lang.String *;
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

##---------------End: proguard configuration for Gson  ----------

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class company.tap.tapcardformkit.** { *; }
# Keep ML Kit classes
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

#########################################
# Retrofit
#########################################
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

# Keep method parameter names (needed for reflection)
-keepattributes Signature, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

#########################################
# OkHttp
#########################################
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

# Keep Okio (used internally by OkHttp)
-keep class okio.** { *; }
-dontwarn okio.**

#########################################
# OkHttp Logging Interceptor
#########################################
-keep class okhttp3.logging.** { *; }
-dontwarn okhttp3.logging.**

#########################################
# Gson
#########################################
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Keep field names annotated with @SerializedName
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Preserve generic type info used by Gson
-keepattributes Signature
-keepattributes *Annotation*

#########################################
# Retrofit + Gson Converter
#########################################
-keep class retrofit2.converter.gson.** { *; }
-dontwarn retrofit2.converter.gson.**

#########################################
# Retrofit - R8 full mode (default since AGP 8.0)
#########################################
# A Retrofit service interface has no compile-time implementor: Retrofit.create()
# returns a java.lang.reflect.Proxy built at runtime. In full mode R8 concludes
# nothing can ever have that type and rewrites the cast on create() into
# "throw ClassCastException if non-null", so the first network call dies with a
# bare CCE. Keeping the annotated interfaces tells R8 they are instantiated.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Retrofit reflects on generic return types (Call<JsonElement>, Response<T>, ...).
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes AnnotationDefault
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# Tap network services are reached through those proxies.
-keep,allowobfuscation interface company.tap.tapnetworkkit.interfaces.** { *; }

-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
