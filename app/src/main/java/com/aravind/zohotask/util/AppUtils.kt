package com.aravind.zohotask.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(dateFormat: String): String =
        SimpleDateFormat(dateFormat).format(Date())
}