package com.pdf

import android.content.res.AssetManager
import java.io.File

object FileUtils {
  fun fromAssetToFile(assetManager: AssetManager, assetFileName: String, outputFile: File) {
    assetManager.open(assetFileName).use { input ->
      outputFile.deleteOnExit()
      outputFile.writeBytes(input.readBytes())
    }
  }
}
