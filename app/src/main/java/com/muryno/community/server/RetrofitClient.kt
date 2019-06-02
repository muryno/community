package com.muryno.community.server

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitClient {
    val BASE_URL = "http://192.168.1.68/api/"

    var mInstance: RetrofitClient? = null

    var mRetrofit: Retrofit? = null
    constructor() {



        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        //  .addHeader(
        //                        "Authorization",
        //                        "Bearer ${SharedPreferenceService.getToken(UrekaLiteApplication.context)}"
        //                    )

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        }.addInterceptor(interceptor).build()


        @Synchronized
        fun getInstance(): RetrofitClient {
            if (mInstance == null) {
                mInstance = RetrofitClient()
            }
            return mInstance!!
        }


        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        fun reset() {
            mInstance = null
            getInstance()
        }
    }
    fun getApi(): ApiInterface {
        return mRetrofit!!.create(ApiInterface::class.java)
    }



}