package com.muryno.community.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Environment
import android.text.TextUtils
import com.muryno.community.MainApplication
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private val SIMPLE_DATE_FORMAT = SimpleDateFormat("dd_mm_yyyy_hh_mm_ss", Locale.ENGLISH)

fun isOnline(): Boolean {

    if (MainApplication.getInstance().applicationContext == null)
        return false

    val cm = MainApplication.getInstance().applicationContext
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    //        assert cm != null;
    //        return cm.getActiveNetworkInfo() != null &&
    //                cm.getActiveNetworkInfo().isConnectedOrConnecting();

    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}

private fun isValidEmail(email: String): Boolean {
    return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun getOutputMediaFile(): File?{
    val mediaStorageDir =
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Community")

    if (!mediaStorageDir.exists()) {
        if (!mediaStorageDir.mkdirs()) {
            return null
        }
    }
    val timeStamp = SIMPLE_DATE_FORMAT.format(Date())
    return File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")

}

@Throws(IOException::class)
fun getCompressedFile(path: String, context: Context?): File? {
    if (context == null)
        return File(path)
    Log(path)
    val fresh = File(path)
    val size = fresh.length() / 1000 //To KB

    //doesn't need compression. Use as it is.
    if (Math.abs(size) <= 400) {
        return fresh
    }


    var cacheDIR = context.externalCacheDir
    if (cacheDIR == null)
        cacheDIR = context.cacheDir
    val tempCacheDIR = cacheDIR!!.absolutePath + "/jhaki/.Temp"
    val tempCacheFile = File(tempCacheDIR)
    if (!tempCacheFile.exists())
        tempCacheFile.mkdirs()

    val bitmap = BitmapUtils.decodeImageFromFiles(path)
    val mainFile = File(tempCacheFile, SIMPLE_DATE_FORMAT.format(Date()) + Random().nextInt(999999) + ".jpg")
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)

    val fileOutputStream = FileOutputStream(mainFile)
    fileOutputStream.write(outputStream.toByteArray())
    fileOutputStream.flush()

    fileOutputStream.close()
    return mainFile
}
