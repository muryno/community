package com.muryno.community.ui.view

interface LoginView:BaseView {
    fun emailError(msg: String)

    fun passError(msg: String)

}