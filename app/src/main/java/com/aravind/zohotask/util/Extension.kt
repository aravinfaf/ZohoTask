package com.aravind.zohotask.util

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.aravind.zohotask.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun ImageView.loadImage(url: String ?) {
    Glide.with(context).load(url)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}