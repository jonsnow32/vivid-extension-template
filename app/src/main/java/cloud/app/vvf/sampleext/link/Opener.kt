package cloud.app.vvf.sampleext.link

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import cloud.app.vvf.extensions.SampleClient
import cloud.app.vvf.sampleext.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class Opener : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {

    setContentView(R.layout.acitvity_opener)
    super.onCreate(savedInstanceState)

    handleUri(intent.data)

    val startBtn = findViewById<MaterialButton>(R.id.startButton)
    startBtn.setOnClickListener {
      val url = findViewById<TextInputEditText>(R.id.textInputEditText).text.toString()
      val uri = Uri.parse(url)
      handleUri(uri)
    }
  }

  private fun handleUri(uri: Uri?) {
    if (uri != null) {
      val type = uri.pathSegments[0]
      val path = when (type) {
        "movie" -> {
          val movieID = uri.pathSegments[1] ?: return
          "movie/$movieID"
        }

        "tv" -> {
          val showID = uri.pathSegments[1] ?: return
          val season = uri.pathSegments[2] ?: return
          val episode = uri.pathSegments[3] ?: return
          "show/$showID/s/$season/e/$episode"
        }
        else -> return
      }
      val extensionId = getString(R.string.class_path)
      val uriString = "vvf://extensions/$extensionId/$path"
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uriString)))
      finishAndRemoveTask()
    }
  }
}
