package com.kim20598.utils

class SIMKLMetadata {
    companion object {
        suspend fun enhanceWithSIMKL(
            title: String,
            year: Int? = null,
            imdbId: String? = null,
            tmdbId: String? = null,
            type: String = "movie"
        ): SIMKLUtils.SIMKLDetails? {
            return when {
                !imdbId.isNullOrBlank() -> SIMKLUtils.getSIMKLByIMDB(imdbId)
                else -> {
                    val searchResults = SIMKLUtils.searchSIMKL(title, type)
                    searchResults.firstOrNull { item ->
                        item.title.equals(title, ignoreCase = true) && 
                        (year == null || item.year == year)
                    }?.ids?.simkl?.let { simklId ->
                        SIMKLUtils.getSIMKLDetails(simklId, type)
                    }
                }
            }
        }
        
        // Return SIMKL data for providers to use in their load methods
        fun getSIMKLMetadata(
            simklData: SIMKLUtils.SIMKLDetails?
        ): SIMKLMetadataResult {
            return SIMKLMetadataResult(
                rating = simklData?.rating?.toDouble(),
                overview = simklData?.overview,
                genres = simklData?.genres,
                runtime = simklData?.runtime,
                posterUrl = simklData?.poster?.let { "https://simkl.in/posters/${it}_m.jpg" },
                backgroundPosterUrl = simklData?.fanart?.let { "https://simkl.in/fanart/${it}_mobile.jpg" }
            )
        }
    }
}

// Data class to hold SIMKL metadata results
data class SIMKLMetadataResult(
    val rating: Double? = null,
    val overview: String? = null,
    val genres: List<String>? = null,
    val runtime: Int? = null,
    val posterUrl: String? = null,
    val backgroundPosterUrl: String? = null
)
