package com.pdf

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.compose.pdf.reader.PdfViewerFragment
import java.io.File
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {

  private val viewCache by lazy { hashMapOf<Int, View>() }

  private inline fun <reified T : View> getViewByIdAndCache(@IdRes id: Int): T {
    val cachedView = viewCache[id]
    if (cachedView != null && cachedView is T) return cachedView
    else if (cachedView != null && cachedView !is T) throw IllegalArgumentException("More than one view has the ID")
    return findViewById<T>(id).also {
      viewCache[id] = it
    }
  }

  private val openSampleBtn: AppCompatButton
    get() = getViewByIdAndCache(R.id.btnOpenSamplePDF)

  private val progressBar: ProgressBar
    get() = getViewByIdAndCache(R.id.loadingSampleProgress)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    openSampleBtn.setOnClickListener {
      openSampleBtn.changeVisibility(false)
      progressBar.changeVisibility(true)
      val file = File(getExternalFilesDir(null), "sample.pdf")
      FileUtils.fromAssetToFile(assets, "sample.pdf", file)
      val pdfViewerFragment = PdfViewerFragment.newInstanceFromAssets(file)
      openSampleBtn.changeVisibility(true)
      progressBar.changeVisibility(false)
      supportFragmentManager.beginTransaction()
        .add(pdfViewerFragment, "PdfViewerFragment")
        .commitAllowingStateLoss()
    }
  }
}
