rootProject.name = "CloudstreamPlugins"

val disabled = listOf<String>()

fun File.eachDir(block: (File) -> Unit) {
    listFiles()?.filter { it.isDirectory }?.forEach { block(it) }
}

File(rootDir, ".").eachDir { dir ->
    if (!disabled.contains(dir.name) && File(dir, "build.gradle.kts").exists()) {
        include(dir.name)
    }
}

// Ensure utils is included
include("utils")
