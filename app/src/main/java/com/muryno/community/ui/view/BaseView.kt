package com.muryno.community.ui.view


interface BaseView {

    fun loadingStart()

    fun loadingFailed(msg: String?)

    fun loadingSuccessful(msg: String?)
}
