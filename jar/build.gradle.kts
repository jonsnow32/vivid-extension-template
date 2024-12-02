
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.google.gson.JsonParser
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

plugins {
  id("java-library")
  alias(libs.plugins.jetbrains.kotlin.jvm)
  alias(libs.plugins.shadow)
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
  jvmToolchain(17)
}
dependencies {
  compileOnly(libs.vividfusion)
  compileOnly(libs.kotlinx.coroutines.core)
}

val extType = "subtitle"
val extId = "subtitle_client_sample"
val extClass = "SubtitleClientSample"

val extIconUrl = "https://music.youtube.com/img/favicon_144.png"
val extName = "SampleSubtitleClient"
val extDescription = "SampleSubtitleClient"

val extAuthor: String by project
val extAuthorUrl: String? by project

val extRepoUrl: String? by project
val extUpdateUrl: String? by project

val authToken: String? by project

val gitHash = execute("git", "rev-parse", "HEAD").take(7)
val gitCount = execute("git", "rev-list", "--count", "HEAD").toInt()
val gitMessage = execute("git", "log", "-1", "--pretty=%B").trim()
val verCode = gitCount
val verName = gitHash

fun execute(vararg command: String): String {
  val outputStream = ByteArrayOutputStream()
  project.exec {
    commandLine(*command)
    standardOutput = outputStream
  }
  return outputStream.toString().trim()
}

tasks {
  // Task for creating the shadow JAR
  val shadowJar by getting(ShadowJar::class) {
    archiveBaseName.set(extId)
    archiveVersion.set(verName)
    archiveExtension.set("vvf")
    manifest {
      attributes(
        mapOf(
          "Extension-Id" to extId,
          "Extension-Type" to extType,
          "Extension-Class" to "cloud.app.vvf.jarsample.$extClass",
          "Extension-Version-Code" to verCode,
          "Extension-Version-Name" to verName,
          "Extension-Icon-Url" to extIconUrl,
          "Extension-Name" to extName,
          "Extension-Description" to extDescription,
          "Extension-Author" to extAuthor,
          "Extension-Author-Url" to extAuthorUrl,
          "Extension-Repo-Url" to extRepoUrl,
          "Extension-Update-Url" to extUpdateUrl
        )
      )
    }
    exclude("META-INF/*.kotlin_module")
    exclude("kotlin/**")
  }

  // Task for creating the GitHub release and uploading the JAR
  val createRelease by creating {
    dependsOn(shadowJar) // Ensure shadowJar is built first
    doLast {
      if (authToken.isNullOrEmpty()) return@doLast

      val githubToken = authToken!! // Replace with your GitHub token
      val releaseTagName = verName // Use the version name as the tag name
      val releaseName = "Pre-Release $verName"
      val releaseBody = gitMessage.replace("\n", "\\n").replace("\"", "\\\"")

      val githubApiUrl = "https://api.github.com/repos/${extAuthor}/vivid-sample-extension/releases"
      try {
        val releaseId = createGitHubRelease(
          githubApiUrl,
          githubToken,
          releaseTagName,
          releaseName,
          releaseBody,
          true
        )
        val uploadUrl = getReleaseUploadUrl(githubApiUrl, releaseId, githubToken)
        uploadReleaseAsset(shadowJar.archiveFile.get().asFile, uploadUrl, githubToken)
      } catch (e: Exception) {
        println("Error: ${e.message}")
      }
    }
  }
}


// Helper functions
fun createGitHubRelease(
  apiUrl: String,
  token: String,
  tag: String,
  name: String,
  body: String,
  isPreRelease: Boolean
): String {
  val url = URL(apiUrl)
  val connection = url.openConnection() as HttpURLConnection
  connection.requestMethod = "POST"
  connection.setRequestProperty("Authorization", "token $token")
  connection.setRequestProperty("Content-Type", "application/json")
  connection.doOutput = true

  println("body= ${body}")
  val payload = """
        {
            "tag_name": "$tag",
            "name": "$name",
            "body": "$body",
            "prerelease": $isPreRelease
        }
    """.trimIndent()


  OutputStreamWriter(connection.outputStream).use { it.write(payload) }

  if (connection.responseCode != HttpURLConnection.HTTP_CREATED) {
    throw RuntimeException("Failed to create release: ${connection.responseCode} - ${connection.responseMessage}")
  }

  val response = connection.inputStream.bufferedReader().readText()
  val jsonResponse = JsonParser.parseString(response).asJsonObject
  return jsonResponse.get("id").asString // Extract release ID
}

fun getReleaseUploadUrl(apiUrl: String, releaseId: String, token: String?): String {
  val url = URL("$apiUrl/$releaseId")
  val connection = url.openConnection() as HttpURLConnection
  connection.requestMethod = "GET"
  connection.setRequestProperty("Authorization", "token $token")

  if (connection.responseCode != HttpURLConnection.HTTP_OK) {
    throw RuntimeException("Failed to fetch upload URL: ${connection.responseCode} - ${connection.responseMessage}")
  }

  val response = connection.inputStream.bufferedReader().readText()
  val jsonResponse = JsonParser.parseString(response).asJsonObject
  return jsonResponse.get("upload_url").asString.replace("{?name,label}", "")
}

fun uploadReleaseAsset(assetFile: File, uploadUrl: String, token: String) {
  val url = URL("$uploadUrl?name=${assetFile.name}")
  val connection = url.openConnection() as HttpURLConnection
  connection.requestMethod = "POST"
  connection.setRequestProperty("Authorization", "token $token")
  connection.setRequestProperty("Content-Type", "application/java-archive")
  connection.doOutput = true

  assetFile.inputStream().use { input ->
    connection.outputStream.use { output ->
      input.copyTo(output)
    }
  }

  if (connection.responseCode != HttpURLConnection.HTTP_CREATED) {
    throw RuntimeException("Failed to upload asset: ${connection.responseCode} - ${connection.responseMessage}")
  }

  println("Asset uploaded successfully: ${assetFile.name}")
}
