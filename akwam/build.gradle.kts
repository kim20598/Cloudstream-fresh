// use an integer for version numbers
version = 4

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

cloudstream {
    language = "ar"
    authors = listOf("kim20598")
    status = 1
    tvTypes = listOf(
        "Movie",
        "TvSeries"
    )
    iconUrl = "https://t2.gstatic.com/faviconV2?client=SOCIAL&amp;type=FAVICON&amp;fallback_opts=TYPE,SIZE,URL&amp;url=https://ak.sv&amp;size=%size%"
    isCrossPlatform = true
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
    }
}

dependencies {
    val implementation by configurations
    implementation(project(":utils"))
}
