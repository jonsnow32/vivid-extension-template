package cloud.app.vvf.extensions.services.trakt.services.model


import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("ids")
    val ids: Ids,
    @SerializedName("name")
    val name: String,
)
