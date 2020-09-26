

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public class * extends java.lang.Exception

-dontwarn okhttp3.**
-dontnote okhttp3.**
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions.*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.MovieFirebase{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.Movie{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.APIMovieCast{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.APIMovie{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.MovieCast{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.Cast{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.APIPerson{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.APIMoviePictures{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.APIMovieVideos{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.Backdrop{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.BelongsToCollection{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.Crew{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.Genre{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.Model{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.MovieCrew{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.Oyuncu{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.Poster{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.ProductionCompany{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.ProductionCountry{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.Result{
  *;
}

-keepclassmembers class com.utkuakgungor.filmtavsiye.models.SpokenLanguage{
  *;
}