# ProGuard & R8 Optimization Rules for StudyMate

# Keep Room generated code
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Keep Moshi & Retrofit Models
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes *Annotation*
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Firebase Models
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.firebase.database.IgnoreExtraProperties <fields>;
    @com.google.firebase.database.IgnoreExtraProperties <methods>;
}

# Keep Coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }

# Preserve line numbers for stack trace debugging
-keepattributes SourceFile,LineNumberTable
