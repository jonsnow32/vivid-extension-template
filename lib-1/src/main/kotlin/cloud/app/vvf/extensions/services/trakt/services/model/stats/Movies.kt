package cloud.app.vvf.extensions.services.trakt.services.model.stats

data class Movies(
    val collected: Int,
    val comments: Int,
    val minutes: Int,
    val plays: Int,
    val ratings: Int,
    val watched: Int
)
