
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.cloud.app.vivid.gradle)
}


viVidExtension {
  iconUrl = "https://www.google.com/s2/favicons?domain=moviehab.com&sz=%size%"
  authors = listOf("jonsnow")
  setRepo("https://github.com/jonsnow32/vivid-sample-extension")
  version = 1
  status = 1
}



android {
  namespace = "cloud.app.vvf.jarsample"
  compileSdk = 34

  defaultConfig {
    minSdk = 24

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
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

  kotlin {
    jvmToolchain(17) // Optional, sets JVM target
  }

  tasks.withType<KotlinCompile> {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
      freeCompilerArgs.add("-Xno-call-assertions")
      freeCompilerArgs.add("-Xno-param-assertions")
      freeCompilerArgs.add("-Xno-receiver-assertions")
    }
  }
}

dependencies {
  compileOnly(libs.vividfusion)
  compileOnly(libs.kotlinx.coroutines.core)
}

//
//val extType = "subtitle"
//val extId = "subtitle_client_sample"
//val extClass = "SubtitleClientSample"
//
//val extIconUrl = "https://music.youtube.com/img/favicon_144.png"
//val extName = "SampleSubtitleClient"
//val extDescription = "SampleSubtitleClient"
//
//val extAuthor: String by project
//val extAuthorUrl: String? by project
//
//val extRepoUrl: String? by project
//val extUpdateUrl: String? by project
//
//val authToken: String? by project
//
//val gitHash = execute("git", "rev-parse", "HEAD").take(7)
//val gitCount = execute("git", "rev-list", "--count", "HEAD").toInt()
//val gitMessage = execute("git", "log", "-1", "--pretty=%B").trim()
//val verCode = gitCount
//val verName = gitHash
//
//fun execute(vararg command: String): String {
//  val outputStream = ByteArrayOutputStream()
//  project.exec {
//    commandLine(*command)
//    standardOutput = outputStream
//  }
//  return outputStream.toString().trim()
//}

