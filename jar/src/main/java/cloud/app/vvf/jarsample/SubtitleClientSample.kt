package cloud.app.vvf.jarsample

import cloud.app.vvf.common.VVFExtension
import cloud.app.vvf.common.clients.subtitles.SubtitleClient
import cloud.app.vvf.common.helpers.network.HttpHelper
import cloud.app.vvf.common.settings.PrefSettings
import cloud.app.vvf.common.settings.Setting

@VVFExtension
class SubtitleClientSample : SubtitleClient{
  override val defaultSettings: List<Setting>
    get() = TODO("Not yet implemented")

  override fun init(prefSettings: PrefSettings, httpHelper: HttpHelper) {
    TODO("Not yet implemented")
  }

  override suspend fun onExtensionSelected() {
    TODO("Not yet implemented")
  }
}
