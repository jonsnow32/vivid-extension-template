import java.io.ByteArrayOutputStream

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
}


val extType = "stream"
val extId = "stream_client_sample"
val extClass = "SampleClient"

val extIconUrl = "https://music.youtube.com/img/favicon_144.png"
val extName = "SampleStreamClient"
val extDescription = "SampleStreamClient"

val extAuthor: String by project
val extAuthorUrl: String? by project

val extRepoUrl: String? by project
val extUpdateUrl: String? by project

val gitHash = execute("git", "rev-parse", "HEAD").take(7)
val gitCount = execute("git", "rev-list", "--count", "HEAD").toInt()
val verCode = gitCount
val verName = gitHash

tasks.register("uninstall") {
  exec {
    isIgnoreExitValue = true
    executable(android.adbExecutable)
    args("shell", "pm", "uninstall", android.defaultConfig.applicationId!!)
  }
}

fun execute(vararg command: String): String {
  val outputStream = ByteArrayOutputStream()
  project.exec {
    commandLine(*command)
    standardOutput = outputStream
  }
  return outputStream.toString().trim()
}


android {
  namespace = "cloud.app.vvf.sampleext"
  compileSdk = 35

  defaultConfig {
    applicationId = "cloud.app.vvf.sampleext"
    minSdk = 24
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    manifestPlaceholders.apply {
      put("type", "cloud.app.vvf.${extType}")
      put("id", extId)
      put("class_path", "cloud.app.vvf.sampleext.${extClass}")
      put("version", verName)
      put("version_code", verCode.toString())
      put("icon_url", extIconUrl)
      put("app_name", "Echo : $extName Extension")
      put("name", extName)
      put("description", extDescription)
      put("author", extAuthor)
      extAuthorUrl?.let { put("author_url", it) }
      extRepoUrl?.let { put("repo_url", it) }
      extUpdateUrl?.let { put("update_url", it) }
    }
    resValue("string", "id", extId)
    resValue("string", "class_path", "$namespace.${extClass}")

    versionName = verName
    resValue("string", "version", verName)
    versionCode = verCode
    resValue("string", "version_code", verCode.toString())

    resValue("string", "icon_url", extIconUrl)
    resValue("string", "app_name", "Echo : $extName Extension")
    resValue("string", "name", extName)
    description?.let { resValue("string", "description", it) }

    resValue("string", "author", extAuthor)
    extAuthorUrl?.let { resValue("string", "author_url", it) }

    extRepoUrl?.let { resValue("string", "repo_url", it) }
    extUpdateUrl?.let { resValue("string", "update_url", it) }
  }


  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  implementation(libs.vividfusion)

}
