import cloud.app.vvf.VvfExtension
import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.jetbrains.kotlin.jvm) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.vivid.gradle) apply false
}

fun Project.vvfExtension(configuration: VvfExtension.() -> Unit) =
  extensions.getByName<VvfExtension>("vvfExtension").configuration()

fun Project.android(configuration: BaseExtension.() -> Unit) =
  extensions.getByName<BaseExtension>("android").configuration()

val extRepoUrl: String? by project
val authToken: String? = project.findProperty("authToken") as String?
  ?: System.getenv("GITHUB_AUTH_TOKEN")

subprojects {
  println("name of subproject: $name") //null here
  if (name != "app") { // Exclude the application module
    apply(plugin = "kotlin-android")
    apply(plugin = "cloud.app.vvf.plugin")

    vvfExtension {
      setRepo(extRepoUrl ?: "https://github.com/jonsnow32/vivid-sample-extension", authToken)
      version = 1
      status = 1
      isPreRelease = true
    }
  }
}
