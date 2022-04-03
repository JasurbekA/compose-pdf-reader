package com.compose.pdf.reader

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.constants.Constants
import java.io.File

class PdfViewerFragment : DialogFragment() {

  companion object {
    fun newInstanceFromAssets(file: File): PdfViewerFragment {
      val args = bundleOf(Constants.BUNDLE_KEY_FILE to file)
      return PdfViewerFragment().apply {
        arguments = args
      }
    }
  }

  private val viewModel: PdfViewerViewModel by lazy {
    ViewModelProvider(this)[PdfViewerViewModel::class.java]
  }

  private val pageDividerColor = Color(0xFFC4C4C4)

  override fun onStart() {
    super.onStart()
    dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return super.onCreateDialog(savedInstanceState).apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    val displayMetrics = context.resources.displayMetrics
    val point = Point().apply {
      x = displayMetrics.widthPixels
      y = displayMetrics.heightPixels
    }
    val file = requireArguments().get(Constants.BUNDLE_KEY_FILE) as File
    viewModel.load(point, file)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return ComposeView(requireContext()).apply {
      setContent {
        Column {
          TopAppBar(
            title = { Text(text = "Title of the book") },
            navigationIcon = {
              IconButton(onClick = ::dismiss) {
                Icon(Icons.Default.ArrowBack, "ArrowBack")
              }
            },
            backgroundColor = Color.White
          )
          PDFScreen(viewModel.imageList)
        }
      }
    }
  }

  @Composable
  fun PDFScreen(pages: SnapshotStateList<ImageBitmap?>) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
    ) {
      items(pages) { item ->
        if (item != null) {
          Image(bitmap = item, contentDescription = "")
          Spacer(
            modifier = Modifier
              .fillMaxWidth()
              .background(pageDividerColor)
              .height(16.dp)
          )
        }
      }
    }
  }
}
