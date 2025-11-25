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
        // Read SIMKL config from local.properties
        val properties = java.util.Properties()
        val localProperties = rootProject.file("local.properties")
        if (localProperties.exists()) {
            properties.load(localProperties.inputStream())
            buildConfigField("String", "SIMKL_CLIENT_ID", "\"${properties.getProperty("SIMKL_CLIENT_ID", "")}\"")
        } else {
            buildConfigField("String", "SIMKL_CLIENT_ID", "\"c01381d4983a1830e7eaef34217e680a9371baa86c99ec08dd13b37a772125cb\"")
        }
    }
}