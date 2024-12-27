import java.io.ByteArrayOutputStream

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
}

val extClass = "SampleClient"

val extIconUrl = "https://music.youtube.com/img/favicon_144.png"
val extName = "Apk Sample Extension"
val extDescription = "This is insalled Apk Sample Extension"

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


vvfExtension {
  iconUrl = extIconUrl
  authors = listOf("jonsnowapp")
  version = 1
  status = 1
}

android {
  namespace = "cloud.app.vvf.sampleext"
  compileSdk = 35

  defaultConfig {
    minSdk = 24
    targetSdk = 35

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    manifestPlaceholders.apply {
      put("class_path", "cloud.app.vvf.extensions.${extClass}")
      put("version", verName)
      put("version_code", verCode.toString())
      put("icon_url", extIconUrl)
      put("app_name", "VVF : $extName Extension")
      put("name", extName)
      put("description", extDescription)
      put("author", extAuthor)
      put("types", "stream") //Declare the extension type so the app knows it before loading the class, ex: "stream,database,subtitle"

      extAuthorUrl?.let { put("author_url", it) }
      extRepoUrl?.let { put("repo_url", it) }
      extUpdateUrl?.let { put("update_url", it) }
    }

    resValue("string", "class_path", "cloud.app.vvf.extensions.${extClass}")
    versionName = verName
    resValue("string", "version", verName)
    versionCode = verCode
    resValue("string", "version_code", verCode.toString())

    resValue("string", "icon_url", extIconUrl)
    resValue("string", "app_name", "VVF : $extName Extension")
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
  implementation(project(":lib-1"))
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  compileOnly(libs.vividfusion)
}
