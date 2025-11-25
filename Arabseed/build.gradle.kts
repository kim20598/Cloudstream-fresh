// use an integer for version numbers
version = 4

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

cloudstream {
    language = "ar"
    authors = listOf("kim20598")
    status = 1
    tvTypes = listOf(
        "Movie",
        "TvSeries",
        "Anime"
    )
    iconUrl = "https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://a.asd.homes&size=%size%"
    isCrossPlatform = true
}

android {
    namespace = "com.arabseed"
    compileSdk = 34
    defaultConfig { minSdk = 21 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { 
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-XXLanguage:+BreakContinueInInlineLambdas")
    }
}

dependencies {
    val implementation by configurations
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation(project(":utils"))
}
