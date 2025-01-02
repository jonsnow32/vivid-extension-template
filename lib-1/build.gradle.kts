import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.android.library)
}

vvfExtension {
  iconUrl = "https://www.google.com/s2/favicons?domain=moviehab.com&sz=%size%"
  authors = listOf("jonsnow2")
  version = 1
  status = 1
  types = listOf("stream","subtitle")
}

android {
  namespace = "cloud.app.vvf"
  defaultConfig {
    minSdk = 24
    compileSdk = 33
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
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
  val compileOnly by configurations
  compileOnly(rootProject.libs.vividfusion)
  compileOnly(rootProject.libs.kotlinx.coroutines.core)
}
