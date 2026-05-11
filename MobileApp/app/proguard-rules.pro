# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Hide the original source file name
-renamesourcefileattribute SourceFile

# === Hilt/Dagger ===
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.HiltAndroidApp
-keepclasseswithmembers class * {
    @dagger.hilt.android.AndroidEntryPoint <methods>;
}

# === Retrofit/OkHttp ===
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# === Gson ===
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# === Room ===
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# === Coroutines ===
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# === Lifecycle ===
-keep class * extends androidx.lifecycle.ViewModel
-keep class * extends androidx.lifecycle.AndroidViewModel
-keep class * extends androidx.lifecycle.LiveData
-keep class * extends androidx.lifecycle.MutableLiveData

# === Data Binding ===
-keep class androidx.databinding.** { *; }
-keep class * extends androidx.viewbinding.ViewBinding { *; }

# === Firebase ===
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-keepclassmembers class * {
    @com.google.firebase.annotations.Ignore <methods>;
}

# === Security ===
-keep class androidx.security.crypto.** { *; }

# === Glide ===
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class * extends com.bumptech.glide.load.resource.bitmap.BitmapEncoder {
  <init>(...);
}

# === Custom Models ===
-keep class com.smarthome.data.model.** { *; }
-keep class com.smarthome.data.database.entities.** { *; }

# === Network Security ===
-keep class com.smarthome.network.** { *; }

# === ViewModels ===
-keep class com.smarthome.ui.** { *; }

# === Native Methods ===
-keepclasseswithmembernames class * {
    native <methods>;
}

# === Enums ===
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# === Parcelable ===
-keep class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# === Serializable ===
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}