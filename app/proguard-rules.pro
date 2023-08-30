-keepattributes SourceFile,LineNumberTable
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


-keepattributes Annotation
-keepattributes Signature
-dontwarn
-ignorewarnings

# Mapbox
#
#-keep public class com.google.firebase.* { public ; }
#-keep class com.google.firebase.* { ; }
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-dontwarn org.xmlpull.v1.**
-dontnote org.xmlpull.v1.**
    # Add this global rule
    -keepattributes Signature

    # This rule will properly ProGuard all the model classes in
    # the package com.yourcompany.models. Modify to fit the structure
    # of your app.
    -keepclassmembers class com.earthmap.satellite.map.location.map.** {
      *;
    }