package cloud.app.vvf.extensions.services.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Logo(
    @SerialName("aspect_ratio")
    val aspectRatio: Double,
    @SerialName("file_path")
    val filePath: String,
    @SerialName("height")
    val height: Int,
    @SerialName("iso_639_1")
    val iso6391: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("width")
    val width: Int,
)
