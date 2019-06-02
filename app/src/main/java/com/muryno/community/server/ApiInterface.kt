package com.muryno.community.server

import com.muryno.community.db.BaseData
import com.muryno.community.db.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @Multipart
    @POST("user/register")
    fun register(
        @Part("fname") fname: RequestBody, @Part("lname") lname: RequestBody, @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody, @Part("password") password: RequestBody,
        @Part picture: MultipartBody.Part
    ): Call<BaseData<User>>

    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("email") email:String,@Field("password") paswrd:String)
            :Call<BaseData<User>>


}