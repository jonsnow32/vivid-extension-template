package cloud.app.vvf.jarsample

import cloud.app.vvf.common.VVFExtension
import cloud.app.vvf.common.clients.streams.StreamClient
import cloud.app.vvf.common.clients.subtitles.SubtitleClient
import cloud.app.vvf.common.helpers.network.HttpHelper
import cloud.app.vvf.common.models.AVPMediaItem
import cloud.app.vvf.common.models.SubtitleData
import cloud.app.vvf.common.models.stream.StreamData
import cloud.app.vvf.common.settings.PrefSettings
import cloud.app.vvf.common.settings.Setting

@VVFExtension
class MultipleClientSample : SubtitleClient, StreamClient{
  override val defaultSettings: List<Setting>
    get() = TODO("Not yet implemented")

  override fun init(prefSettings: PrefSettings, httpHelper: HttpHelper) {
    TODO("Not yet implemented")
  }

  override suspend fun loadLinks(
    mediaItem: AVPMediaItem,
    subtitleCallback: (SubtitleData) -> Unit,
    callback: (StreamData) -> Unit
  ): Boolean {
    TODO("Not yet implemented")
  }

  override suspend fun onExtensionSelected() {
    TODO("Not yet implemented")
  }
}
