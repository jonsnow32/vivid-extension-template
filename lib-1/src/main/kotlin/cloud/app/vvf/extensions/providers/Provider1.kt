package cloud.app.vvf.extensions.providers

import cloud.app.vvf.common.helpers.network.HttpHelper
import cloud.app.vvf.common.models.AVPMediaItem
import cloud.app.vvf.common.models.SubtitleData
import cloud.app.vvf.common.models.stream.StreamData
import kotlinx.coroutines.delay

class Provider1(httpHelper: HttpHelper) : WebScraper(httpHelper) {
  override val name: String
    get() = "sampleDomain"
  override val baseUrl: String
    get() = "https://sampledomain.com"

  override suspend fun invoke(
    avpMediaItem: AVPMediaItem,
    subtitleCallback: (SubtitleData) -> Unit,
    callback: (StreamData) -> Unit
  ) {
    var imdbId: String? = null
    var tmdbId: Int? = null
    var episode : Int? = null
    var season : Int? = null

    when(avpMediaItem) {
      is AVPMediaItem.MovieItem -> {
        imdbId = avpMediaItem.movie.ids.imdbId
        tmdbId = avpMediaItem.movie.ids.tmdbId
      }
      is AVPMediaItem.EpisodeItem ->  {
        imdbId = avpMediaItem.seasonItem.showItem.show.ids.imdbId
        tmdbId = avpMediaItem.seasonItem.showItem.show.ids.tmdbId
        episode = avpMediaItem.episode.episodeNumber
        season = avpMediaItem.seasonItem.season.number
      }
      else -> {
        TODO("not supported")
      }
    }

    for(i in 1..10) {
      delay(500)
      callback.invoke(
        StreamData("$baseUrl/$imdbId/$i.mp4", "$baseUrl/$imdbId/$i"))
    }

  }

}
