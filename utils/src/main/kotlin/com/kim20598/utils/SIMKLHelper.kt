package com.kim20598.utils

import com.lagradost.cloudstream3.app
import com.fasterxml.jackson.annotation.JsonProperty

object SIMKLHelper {
    private const val CLIENT_ID = "c01381d4983a1830e7eaef34217e680a9371baa86c99ec08dd13b37a772125cb"
    private const val API_URL = "https://api.simkl.com"
    
    suspend fun getMovieDetails(imdbId: String?): SIMKLDetails? {
        if (imdbId.isNullOrBlank()) return null
        
        return try {
            val response = app.get(
                "$API_URL/movies/$imdbId",
                headers = mapOf(
                    "simkl-api-key" to CLIENT_ID,
                    "Content-Type" to "application/json"
                )
            )
            response.parsedSafe<SIMKLDetails>()
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun getTVDetails(imdbId: String?): SIMKLDetails? {
        if (imdbId.isNullOrBlank()) return null
        
        return try {
            val response = app.get(
                "$API_URL/tv/$imdbId",
                headers = mapOf(
                    "simkl-api-key" to CLIENT_ID,
                    "Content-Type" to "application/json"
                )
            )
            response.parsedSafe<SIMKLDetails>()
        } catch (e: Exception) {
            null
        }
    }
    
    fun extractImdbId(text: String): String? {
        return Regex("tt\\d+").find(text)?.value
    }
    
    data class SIMKLDetails(
        @JsonProperty("title") val title: String?,
        @JsonProperty("year") val year: Int?,
        @JsonProperty("ids") val ids: SIMKLIds?,
        @JsonProperty("overview") val overview: String?,
        @JsonProperty("genres") val genres: List<String>?,
        @JsonProperty("runtime") val runtime: Int?,
        @JsonProperty("rating") val rating: Float?,
        @JsonProperty("poster") val poster: String?,
        @JsonProperty("fanart") val fanart: String?
    )
    
    data class SIMKLIds(
        @JsonProperty("simkl") val simkl: Int?,
        @JsonProperty("imdb") val imdb: String?,
        @JsonProperty("tmdb") val tmdb: String?
    )
}
