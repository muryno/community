package com.muryno.community.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.muryno.community.ui.interfaces.IImageCompressionTaskListener
import java.io.File
import java.io.IOException
import java.util.ArrayList

class ImageCompressionTask: Runnable {

    private val mHandler = Handler(Looper.getMainLooper())
    private var iImageCompressionTaskListener: IImageCompressionTaskListener? = null
    private var mID: Int = 0
    private var mutlipleToCompress: List<String>? = null
    private var mContext: Context? = null
    private val compressed = ArrayList<File>()

    constructor( context: Context,
        taskListener: IImageCompressionTaskListener,
                 source: List<String>,
        id: Int
    ) {

        mContext = context
        iImageCompressionTaskListener = taskListener
        mID = id
        var file = context.externalCacheDir
        if (file == null)
            file = context.cacheDir

        val location = file!!.absolutePath + "/community"
        val mCacheLocation = File(location)
        if (!mCacheLocation.exists())
            mCacheLocation.mkdirs()
        mutlipleToCompress = source
    }


    override fun run() {
        try {
            for (path in this.mutlipleToCompress!!) {
                val file = getCompressedFile(path, mContext)
                file?.let { compressed.add(it) }
            }
            mHandler.post { iImageCompressionTaskListener?.onCompressed(compressed, mID) }
        } catch (io: IOException) {
            mHandler.post { iImageCompressionTaskListener?.onError(io) }
        }


    }
}