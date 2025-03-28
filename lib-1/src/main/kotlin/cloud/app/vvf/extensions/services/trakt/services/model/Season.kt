package cloud.app.vvf.extensions.services.trakt.services.model


import com.google.gson.annotations.SerializedName

data class Season(
    @SerializedName("ids")
    val ids: Ids,
    @SerializedName("number")
    val number: Int,
)
