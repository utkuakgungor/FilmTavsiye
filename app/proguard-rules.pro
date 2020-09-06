

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes Signature

-keepattributes Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**



-keepclassmembers class com.utkuakgungor.filmtavsiye.utils.Movie {
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.utils.Oyuncu {
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.utils.Yonetmen {
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.utils.ListsModel {
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.utils.ListsMovieModel {
  *;
}

