package com.compose.pdf.reader

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.sqrt

@Suppress("BlockingMethodInNonBlockingContext")
class PdfViewerViewModel : ViewModel() {

  // TODO this logic is temporary, will optimize it with PDF Feature
  val imageList = mutableStateListOf<ImageBitmap?>(null)

  fun load(screenSize: Point, file: File) {
    viewModelScope.launch {
      val input = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
      val renderer = PdfRenderer(input)
      renderer.use {
        for (i in 0 until renderer.pageCount) {
          val page = renderer.openPage(i)
          val bitmap = Bitmap.createBitmap(screenSize.x, (screenSize.x * sqrt(2f)).toInt(), Bitmap.Config.ARGB_8888)
          page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
          imageList.add(bitmap.asImageBitmap())
          page.close()
        }
      }
    }
  }
}
