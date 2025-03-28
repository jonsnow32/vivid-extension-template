package cloud.app.vvf.extensions.services.trakt.services.model


import com.google.gson.annotations.SerializedName

data class FeatureListResultItem(
    @SerializedName("comment_count")
    val commentCount: Int,
    @SerializedName("like_count")
    val likeCount: Int,
    @SerializedName("list")
    val list: FeatureList,
)
