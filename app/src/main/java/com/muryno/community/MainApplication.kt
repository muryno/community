package com.muryno.community

import androidx.multidex.MultiDexApplication
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object MainApplication: MultiDexApplication() {
    private val executorService = Executors.newCachedThreadPool()
    private var instance: MainApplication? = null

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    fun getExecutorService(): ExecutorService {
        return executorService
    }

    fun getInstance(): MainApplication {
        return instance!!
    }
}