package cloud.app.vvf.jarsample.providers

import cloud.app.vvf.common.helpers.network.HttpHelper
import cloud.app.vvf.common.models.AVPMediaItem
import cloud.app.vvf.common.models.SubtitleData
import cloud.app.vvf.common.models.stream.StreamData
import cloud.app.vvf.jarsample.utils.apmap
import org.jsoup.Jsoup

class RidoMoviesScraper(httpHelper: HttpHelper) : WebScraper(httpHelper) {
  override val name: String
    get() = "RidoMovies"
  override val baseUrl: String
    get() = "https://ridomovies.tv"

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
    val mediaSlug = httpHelper.get("$baseUrl/core/api/search?q=$imdbId")
      .parsedSafe<RidoSearch>()?.data?.items?.find {
        it.contentable?.tmdbId == tmdbId || it.contentable?.imdbId == imdbId
      }?.slug ?: return

    val id = season?.let {
      val episodeUrl = "$baseUrl/tv/$mediaSlug/season-$it/episode-$episode"
      httpHelper.get(episodeUrl).text.substringAfterLast("""postid\":\"""").substringBefore("""\""")
    } ?: mediaSlug

    val url =
      "$baseUrl/core/api/${if (season == null) "movies" else "episodes"}/$id/videos"
    httpHelper.get(url).parsedSafe<RidoResponses>()?.data?.apmap { link ->
      val iframe = Jsoup.parse(link.url ?: return@apmap).select("iframe").attr("data-src")
      if (iframe.startsWith("https://closeload.top")) {
        val unpacked = getAndUnpack(httpHelper.get(iframe, referer = "$baseUrl/").text)
        val video = Regex("=\"(aHR.*?)\";").find(unpacked)?.groupValues?.get(1)
 //       callback.invoke(
 //         StreamData()
//          ExtractorLink(
//            "Ridomovies",
//            "Ridomovies",
//            base64Decode(video ?: return@apmap),
//            "${getBaseUrl(iframe)}/",
//            Qualities.P1080.value,
//            isM3u8 = true
//          )
 //       )
      } else {
       // loadExtractor(iframe, "$baseUrl/", subtitleCallback, callback)
      }
    }
  }

  data class RidoContentable(
    var imdbId: String? = null,
    var tmdbId: Int? = null,
  )

  data class RidoItems(
    var slug: String? = null,
    var contentable: RidoContentable? = null,
  )


  data class RidoData(
    var url: String? = null,
    var items: ArrayList<RidoItems>? = arrayListOf(),
  )

  data class RidoSearch(var data: RidoData? = null)

  data class RidoResponses(
    var data: ArrayList<RidoData>? = arrayListOf(),
  )
}
