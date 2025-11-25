// use an integer for version numbers
version = 4

cloudstream {
    language = "ar"
    authors = listOf("kim20598")
    status = 1
    tvTypes = listOf(
        "Movie",
        "TvSeries"
        // Add other types as needed
    )
    iconUrl = "https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://egydead.skin&size=%size%"
    isCrossPlatform = true
}

dependencies {
    val implementation by configurations
    // Add utils module dependency for SIMKL integration
    implementation(project(":utils"))
}
