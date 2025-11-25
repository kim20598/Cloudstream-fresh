package com.kim20598.utils

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.AppUtils.parseJson

object SIMKLUtils {
    
    suspend fun searchSIMKL(query: String, type: String = "all"): List<SIMKLItem> {
        return try {
            val url = "${SIMKLConfig.SIMKL_API_URL}${SIMKLConfig.SEARCH_ENDPOINT}/$type"
            val response = app.get(
                url,
                headers = SIMKLConfig.getHeaders(),
                params = mapOf("q" to query, "limit" to "10")
            )
            
            response.parsedSafe<SIMKLSearchResponse>()?.results ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getSIMKLDetails(simklId: Int, type: String): SIMKLDetails? {
        return try {
            val endpoint = when (type) {
                "movie" -> SIMKLConfig.MOVIE_ENDPOINT
                "tv" -> SIMKLConfig.TV_ENDPOINT
                "anime" -> SIMKLConfig.ANIME_ENDPOINT
                else -> SIMKLConfig.MOVIE_ENDPOINT
            }
            val url = "${SIMKLConfig.SIMKL_API_URL}$endpoint/$simklId"
            val response = app.get(
                url,
                headers = SIMKLConfig.getHeaders(),
                params = mapOf("extended" to "full")
            )
            
            response.parsedSafe<SIMKLDetails>()
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun getSIMKLByIMDB(imdbId: String): SIMKLDetails? {
        return try {
            val url = "${SIMKLConfig.SIMKL_API_URL}${SIMKLConfig.SEARCH_ENDPOINT}/id"
            val response = app.get(
                url,
                headers = SIMKLConfig.getHeaders(),
                params = mapOf(
                    "imdb" to imdbId,
                    "extended" to "full"
                )
            )
            
            response.parsedSafe<SIMKLDetails>()
        } catch (e: Exception) {
            null
        }
    }
    
    data class SIMKLSearchResponse(
        @JsonProperty("results") val results: List<SIMKLItem>? = emptyList()
    )
    
    data class SIMKLItem(
        @JsonProperty("title") val title: String?,
        @JsonProperty("year") val year: Int?,
        @JsonProperty("ids") val ids: SIMKLIds?,
        @JsonProperty("type") val type: String?
    )
    
    data class SIMKLIds(
        @JsonProperty("simkl") val simkl: Int?,
        @JsonProperty("slug") val slug: String?,
        @JsonProperty("tmdb") val tmdb: String?,
        @JsonProperty("imdb") val imdb: String?,
        @JsonProperty("mal") val mal: String?
    )
    
    data class SIMKLDetails(
        @JsonProperty("title") val title: String?,
        @JsonProperty("year") val year: Int?,
        @JsonProperty("ids") val ids: SIMKLIds?,
        @JsonProperty("overview") val overview: String?,
        @JsonProperty("genres") val genres: List<String>?,
        @JsonProperty("status") val status: String?,
        @JsonProperty("runtime") val runtime: Int?,
        @JsonProperty("rating") val rating: Float?,
        @JsonProperty("poster") val poster: String?,
        @JsonProperty("fanart") val fanart: String?
    )
    
    fun extractIMDBIdFromUrl(url: String): String? {
        val patterns = listOf(
            Regex("tt\\d+"),
            Regex("imdb\\.com/title/(tt\\d+)"),
            Regex("title/(tt\\d+)")
        )
        
        patterns.forEach { pattern ->
            pattern.find(url)?.groups?.get(1)?.value?.let { return it }
            pattern.find(url)?.value?.let { return it }
        }
        
        return null
    }
}