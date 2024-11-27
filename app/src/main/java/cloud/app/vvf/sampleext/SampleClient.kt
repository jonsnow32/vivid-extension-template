package cloud.app.vvf.sampleext

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
import cloud.app.vvf.sampleext.providers.RidoMoviesScraper
import okhttp3.OkHttpClient

@VVFExtension
class SampleClient : StreamClient{
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
    try {
      RidoMoviesScraper(httpHelper).invoke(mediaItem, subtitleCallback, callback)
      return true;
    } catch (e: Exception) {
      Log.e("vvf", e.message, e)
      return false
    }
  }

  override fun init(
    prefSettings: PrefSettings,
    httpHelper: HttpHelper
  ) {
    this.httpHelper = httpHelper
  }

  override suspend fun onExtensionSelected() {

  }
}
