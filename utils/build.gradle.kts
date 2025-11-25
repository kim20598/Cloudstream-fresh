// use an integer for version numbers
version = 1

cloudstream {
    description = "Shared utilities for Kim20598 providers"
    authors = listOf("kim20598")
    status = 1
    tvTypes = listOf("All")
    language = "en"
}

android {
    namespace = "com.kim20598.utils"
    
    buildFeatures {
        buildConfig = true
    }
    
    defaultConfig {
        // Use direct SIMKL client ID without local.properties for now
        buildConfigField("String", "SIMKL_CLIENT_ID", "\"c01381d4983a1830e7eaef34217e680a9371baa86c99ec08dd13b37a772125cb\"")
    }
}

dependencies {
    val implementation by configurations
    val cloudstream by configurations

    cloudstream("com.lagradost:cloudstream3:pre-release")
    implementation("com.github.Blatzar:NiceHttp:0.4.13")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.jsoup:jsoup:1.21.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("com.google.code.gson:gson:2.10.1")
}
