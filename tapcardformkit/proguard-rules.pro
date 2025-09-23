# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class androidx.camera.** { *; }
-keep interface androidx.camera.** { *; }

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

