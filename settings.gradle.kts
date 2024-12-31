pluginManagement {
  resolutionStrategy {
    eachPlugin {
      requested.apply {
        if(requested.id.toString() == "cloud.app.vvf.plugin")
          useModule("com.github.jonsnow32:vivid-gradle:v1.0.0")
      }
    }
  }

  repositories {
    mavenLocal()
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    gradlePluginPortal()
  }

}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenLocal()
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
  }
}

rootProject.name = "sampleExt"

val disabled = listOf<String>()

File(rootDir, ".").eachDir { dir ->
  if (!disabled.contains(dir.name) && File(dir, "build.gradle.kts").exists()) {
    include(dir.name)
  }
}

fun File.eachDir(block: (File) -> Unit) {
  listFiles()?.filter { it.isDirectory }?.forEach { block(it) }
}
