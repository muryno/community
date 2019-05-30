package com.muryno.community.ui.interfaces

import java.io.File

interface IImageCompressionTaskListener {
    fun onCompressed(file: List<File>, id: Int)
    fun onError(throwable: Throwable)
}