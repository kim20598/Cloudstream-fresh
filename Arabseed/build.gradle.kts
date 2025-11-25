// use an integer for version numbers
version = 4

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

android {
    namespace = "com.akwam"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

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
    val cloudstream by configurations
    val implementation by configurations

    // CloudStream core
    cloudstream("com.lagradost:cloudstream3:pre-release")

    // HTTP and networking
    implementation("com.github.Blatzar:NiceHttp:0.4.13")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    
    // JSON parsing
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    
    // HTML parsing
    implementation("org.jsoup:jsoup:1.21.2")
    
    // Kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Other utilities
    implementation("org.mozilla:rhino:1.7.14")
    implementation("me.xdrop:fuzzywuzzy:1.4.0")
    
    // Android UI components
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.browser:browser:1.9.0")
    implementation("androidx.room:room-ktx:2.8.0")
    
    // Kotlin stdlib
    implementation(kotlin("stdlib"))
    
    // Android annotations
    implementation("androidx.annotation:annotation:1.9.1")
}

cloudstream {
    language = "ar"

    authors = listOf("kim20598")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     */
    status = 1

    tvTypes = listOf(
        "Movie",
        "TvSeries",
        "Anime"
    )

    iconUrl = "https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://a.asd.homes&size=%size%"
    isCrossPlatform = true
}
