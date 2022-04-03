package com.pdf

import android.view.View

fun View.changeVisibility(isVisible: Boolean, invisibleType: () -> Int = { View.GONE }) {
  visibility = if (isVisible) View.VISIBLE else invisibleType()
}
