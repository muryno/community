package com.muryno.community.utils

private val TAG = "Jhaki Carrier"

fun Log(data: String, throwable: Throwable) {
    Log(data + "_", throwable)
}

fun Log(throwable: Throwable) {
    Log("ERROR", throwable)
}

fun Log(data: String) {
    android.util.Log.d(TAG, data + "_")
}
