package com.akwam

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.Jsoup
import java.net.URLEncoder
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import org.jsoup.nodes.Element
import kotlin.Pair
import com.kim20598.utils.SIMKLHelper

class Akwam : MainAPI() {
    // ... keep all your existing code the same until the load function ...

    override suspend fun load(url: String): LoadResponse {
        val parts = url.split("#")
        val pageUrl = parts[0]
        val poster = parts.getOrNull(1)?.ifBlank { null }
        val defaultHeaders = mapOf("Referer" to mainUrl)

        val mainDoc = app.get(pageUrl, headers = defaultHeaders).document
        val title = mainDoc.selectFirst("h1.entry-title")?.text()?.trim() ?: "Unknown"
        val plot = mainDoc.selectFirst("h2:contains(قصة المسلسل) + div > p")?.text()?.trim()
            ?: mainDoc.selectFirst("meta[name=description]")?.attr("content")?.trim()
        
        val tags = mainDoc.select("div.font-size-16.text-white a[href*='/genre/'], div.font-size-16.text-white a[href*='/category/']")
            .map { it.text() }
        val year = mainDoc.select("div.font-size-16.text-white a[href*='/year/']").firstOrNull()?.text()?.toIntOrNull()

        // SIMKL Integration - Extract IMDB ID
        val imdbId = SIMKLHelper.extractImdbId(mainDoc.html())
        val simklDetails = if (imdbId != null) {
            // Determine if it's a movie or TV series
            val isSeries = mainDoc.select("div#series-episodes div[class*='col-']").isNotEmpty() ||
                          mainDoc.select("div.widget-body > a.btn[href*='/series/']").isNotEmpty()
            if (isSeries) {
                SIMKLHelper.getTVDetails(imdbId)
            } else {
                SIMKLHelper.getMovieDetails(imdbId)
            }
        } else null

        val recommendations = mainDoc.select("div.widget-body div[class*='col-']").mapNotNull {
            val recTitle = it.selectFirst("h3 a")?.text()?.trim() ?: return@mapNotNull null
            val recHref = it.selectFirst("a")?.attr("href") ?: return@mapNotNull null
            val recPoster = getPoster(it)
            val urlWithPoster = "$recHref#${recPoster ?: ""}"

            newMovieSearchResponse(recTitle, urlWithPoster, TvType.Movie) {
                this.posterUrl = recPoster
            }
        }

        val seasonsMap = linkedMapOf<String, Pair<String, String>>()
        val currentSeasonName = mainDoc.selectFirst("h1.entry-title")?.text()?.trim() ?: title
        seasonsMap[pageUrl] = Pair(currentSeasonName, pageUrl)

        val seasonSelector = "div.widget-body > a.btn[href*='/series/']"
        mainDoc.select(seasonSelector).forEach { a ->
            val href = a.attr("href")
            if (href.isNotBlank()) {
                val seasonUrl = if (href.startsWith("http")) href else "$mainUrl$href"
                val seasonName = a.text().trim()
                if (!seasonsMap.containsKey(seasonUrl)) {
                    seasonsMap[seasonUrl] = Pair(seasonName, seasonUrl)
                }
            }
        }

        val directEpisodes = mainDoc.select("div#series-episodes div[class*='col-']")
        val isSeries = seasonsMap.size > 1 || directEpisodes.isNotEmpty()

        if (!isSeries) {
            return newMovieLoadResponse(name = title, url = pageUrl, type = TvType.Movie, dataUrl = pageUrl) {
                this.posterUrl = poster
                this.backgroundPosterUrl = poster
                this.plot = plot ?: simklDetails?.overview
                this.year = year ?: simklDetails?.year
                this.tags = tags
                this.recommendations = recommendations
                // Add SIMKL rating if available
                simklDetails?.rating?.let { this.score = it / 2 } // Convert 10-point scale to 5-point
            }
        }

        val sortedSeasons = seasonsMap.values.sortedBy { getSeasonNumber(it.first) }
        val allEpisodes = mutableListOf<Episode>()
        val docCache = mutableMapOf(pageUrl to mainDoc)

        for ((seasonName, seasonUrl) in sortedSeasons) {
            val seasonNumber = getSeasonNumber(seasonName)
            val seasonDoc = docCache.getOrPut(seasonUrl) {
                app.get(seasonUrl, headers = defaultHeaders).document
            }

            seasonDoc.select("div#series-episodes div.col-lg-4, div#series-episodes div.col-md-6").forEach { episodeContainer ->
                val episodeLink = episodeContainer.selectFirst("a[href*='/episode/']") ?: return@forEach
                val epUrl = episodeLink.attr("abs:href")
                val epName = episodeLink.selectFirst("h2")?.text()?.trim() ?: episodeLink.text().trim()
                val epPoster = getPoster(episodeContainer)

                if (epUrl.isNotBlank() && epName.isNotBlank()) {
                    allEpisodes.add(newEpisode(epUrl) {
                        name = epName
                        this.season = seasonNumber
                        this.episode = getEpisodeNumberFromString(epName)
                        this.posterUrl = epPoster
                    })
                }
            }
        }

        if (allEpisodes.isEmpty()) {
            return newMovieLoadResponse(name = title, url = pageUrl, type = TvType.Movie, dataUrl = pageUrl) {
                this.posterUrl = poster
                this.backgroundPosterUrl = poster
                this.plot = plot ?: simklDetails?.overview
                this.year = year ?: simklDetails?.year
                this.tags = tags
                this.recommendations = recommendations
                simklDetails?.rating?.let { this.score = it / 2 }
            }
        }

        return newTvSeriesLoadResponse(
            name = title, url = pageUrl, type = TvType.TvSeries, episodes = allEpisodes
        ) {
            this.posterUrl = poster
            this.backgroundPosterUrl = poster
            this.plot = plot ?: simklDetails?.overview
            this.year = year ?: simklDetails?.year
            this.tags = tags
            this.recommendations = recommendations
            simklDetails?.rating?.let { this.score = it / 2 }
        }
    }

    // ... keep the rest of your existing code the same ...
}
