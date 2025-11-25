package com.kim20598.utils

import com.lagradost.cloudstream3.*

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
        
        fun applySIMKLToLoadResponse(
            simklData: SIMKLUtils.SIMKLDetails?,
            builder: LoadResponse.Builder.() -> Unit
        ) {
            simklData?.let { data ->
                builder.apply {
                    data.rating?.let { rating ->
                        this.score = Score.from10(rating.toDouble())
                    }
                    data.overview?.let { overview ->
                        if (this.plot.isNullOrEmpty()) {
                            this.plot = overview
                        }
                    }
                    data.genres?.let { genres ->
                        if (this.tags.isNullOrEmpty()) {
                            this.tags = genres
                        }
                    }
                    data.runtime?.let { runtime ->
                        if (this.duration == null) {
                            this.duration = runtime
                        }
                    }
                    data.poster?.let { poster ->
                        if (this.posterUrl.isNullOrEmpty()) {
                            this.posterUrl = "https://simkl.in/posters/${poster}_m.jpg"
                        }
                    }
                    data.fanart?.let { fanart ->
                        if (this.backgroundPosterUrl.isNullOrEmpty()) {
                            this.backgroundPosterUrl = "https://simkl.in/fanart/${fanart}_mobile.jpg"
                        }
                    }
                }
            }
        }
    }
}