package com.kim20598.utils

object SIMKLConfig {
    // SIMKL API Configuration
    const val SIMKL_API_URL = "https://api.simkl.com"
    
    // API Endpoints
    const val SEARCH_ENDPOINT = "/search"
    const val MOVIE_ENDPOINT = "/movies"
    const val TV_ENDPOINT = "/tv"
    const val ANIME_ENDPOINT = "/anime"
    
    // Get Client ID from BuildConfig (set in build.gradle.kts)
    val SIMKL_CLIENT_ID: String
        get() = if (BuildConfig.SIMKL_CLIENT_ID.isNotEmpty()) {
            BuildConfig.SIMKL_CLIENT_ID
        } else {
            "c01381d4983a1830e7eaef34217e680a9371baa86c99ec08dd13b37a772125cb"
        }
    
    // Headers
    fun getHeaders(): Map<String, String> {
        return mapOf(
            "simkl-api-key" to SIMKL_CLIENT_ID,
            "Content-Type" to "application/json",
            "User-Agent" to "CloudStream-Kim20598/1.0"
        )
    }
}