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
-keep class com.amrabdelhamiddiab.core.** { *; }
-keep class com.amrabdelhamiddiab.core.domain.Constants.**{*;}
-keepattributes *Annotation*

-keepattributes InnerClasses,EnclosingMethod
-optimizations !class/merging/*
-keep class com.google.* {*;}
-keep class com.google.impl.* {*;}
-keep class com.google.firebase.* {*;}
-keep class com.google.googlesignin.** { *; }
-keepnames class com.google.googlesignin.* { *; }
-keep class com.google.gms.** {*;}
-keep class com.google.android.gms.auth.** { *; }
-keep class com.google.android.* {*;}
-keep class com.google.unity.* {*;}

-keep class com.firebase.** { *; }

-keep class org.apache.** { *; }
-keep class com.amrabdelhamiddiab.waiting.framework.utilis.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }

-keep class com.facebook.** {*;}
-keep class com.facebook.internal.** {*;}
-keep class com.facebook.unity.** {*;}

-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**
-dontwarn com.firebase.**
-dontnote com.firebase.client.core.GaePlatform
-keepattributes Signature