// Add this import at the top
import com.kim20598.utils.SIMKLHelper

// In the load method, add SIMKL integration:
override suspend fun load(url: String): LoadResponse {
    val doc = app.get(url).document
    val title = doc.selectFirst("h1.post__name")?.text()?.trim() ?: "غير معروف"
    val poster = doc.selectFirst(".poster__side img, .single__cover img, .post__poster img")?.let { img ->
        (img.attr("data-src").ifBlank { img.attr("src") }).toAbsolute()
    }
    val synopsis = doc.selectFirst(".post__story > p")?.text()?.trim()

    // SIMKL Integration - Extract IMDB ID
    val imdbId = SIMKLHelper.extractImdbId(doc.html())
    val simklDetails = if (imdbId != null) {
        // Determine if it's a movie or TV series
        val isSeries = doc.select("ul.episodes__list li a").isNotEmpty() || 
                      doc.select("div.load__more__episodes").isNotEmpty() ||
                      url.contains("/selary/")
        if (isSeries) {
            SIMKLHelper.getTVDetails(imdbId)
        } else {
            SIMKLHelper.getMovieDetails(imdbId)
        }
    } else null

    // ... keep the rest of your existing episodes code the same ...

    val isTvSeries = episodes.isNotEmpty() || url.contains("/selary/")

    return if (isTvSeries) {
        newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes.distinct().reversed()) {
            this.posterUrl = poster
            this.plot = synopsis ?: simklDetails?.overview
            simklDetails?.rating?.let { this.score = it / 2 }
        }
    } else {
        newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl = poster
            this.plot = synopsis ?: simklDetails?.overview
            simklDetails?.rating?.let { this.score = it / 2 }
        }
    }
}
