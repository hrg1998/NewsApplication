package com.hrg.myapplication.utils.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowMetrics
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.active() {
    isEnabled = true
}

fun View.deactive() {
    isEnabled = false
}

inline fun <reified T> String.toConvertStringJsonToModel(type: Class<T>): T {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val adapter: JsonAdapter<T> = moshi.adapter(type)
    return adapter.fromJson(this)!!
}

inline fun <reified T> T.toConvertModelToJson(type: Class<T>): String {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val adapter: JsonAdapter<T> = moshi.adapter(type)
    return adapter.toJson(this)
}

fun ImageView.loadImage(resourceId: Int) {
    Glide.with(this).load(resourceId).into(this)
}

fun ImageView.loadImage(url: String?) {
    Glide.with(this).load(url).into(this)
}

fun Fragment.toastWhenDebug(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Activity.getWindowBounds(): Pair<Int, Int> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        Pair(
            windowMetrics.bounds.width() - insets.left - insets.right,
            windowMetrics.bounds.height() - insets.bottom - insets.top
        )
    } else {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }
}