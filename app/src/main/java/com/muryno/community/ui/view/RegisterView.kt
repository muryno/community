package com.muryno.community.ui.view

import java.io.File

interface RegisterView:BaseView {
    fun updateImage(file: File)

    fun firstnameError(msg: String)

    fun lastnameError(msg: String)

    fun phoneError(msg: String)

    fun emailError(msg: String)

    fun passError(msg: String)


}