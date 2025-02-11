package cloud.app.vvf.extensions

import android.util.Log
import cloud.app.vvf.common.VVFExtension
import cloud.app.vvf.common.clients.streams.StreamClient
import cloud.app.vvf.common.helpers.network.HttpHelper
import cloud.app.vvf.common.models.AVPMediaItem
import cloud.app.vvf.common.models.SubtitleData
import cloud.app.vvf.common.models.stream.StreamData
import cloud.app.vvf.common.settings.PrefSettings
import cloud.app.vvf.common.settings.Setting
import cloud.app.vvf.common.settings.SettingMultipleChoice
import cloud.app.vvf.extensions.providers.Provider1
import cloud.app.vvf.extensions.utils.amap

@VVFExtension
class SampleClient : StreamClient {
  private lateinit var httpHelper: HttpHelper

  override val defaultSettings: List<Setting> = listOf(
    SettingMultipleChoice(
      "Providers",
      "active_providers_key",
      "Select Provider to Enable",
      entryTitles = listOf("website1", "website2", "website3", "website4"),
      entryValues = listOf("website1", "website2", "website3", "website4"),
      defaultEntryIndices = setOf(0,1,2,3)
    ),
  )

  override suspend fun loadLinks(
    mediaItem: AVPMediaItem,
    subtitleCallback: (SubtitleData) -> Unit,
    callback: (StreamData) -> Unit
  ): Boolean {
    val providers = settings.getStringSet("active_providers_key")
    providers?.toList()?.amap {
      try {
        getProvider(it).invoke(mediaItem, subtitleCallback, callback)
      } catch (e: Exception) {
        Log.e("vvf", e.message, e)
      }
    }
    return true
  }

  private fun getProvider(name: String) = when(name) {
    "website1" -> Provider1(httpHelper)
    else -> throw Exception("Provider not found")
  }

  private lateinit var settings: PrefSettings;

  override fun init(
    prefSettings: PrefSettings,
    httpHelper: HttpHelper
  ) {
    this.httpHelper = httpHelper
    this.settings = prefSettings
  }

  override suspend fun onExtensionSelected() {

  }
}
