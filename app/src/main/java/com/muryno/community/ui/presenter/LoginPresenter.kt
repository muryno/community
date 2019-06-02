package com.muryno.community.ui.presenter

import com.muryno.community.db.BaseData
import com.muryno.community.db.StorePreferences
import com.muryno.community.db.model.User
import com.muryno.community.server.RetrofitClient
import com.muryno.community.ui.view.LoginView
import com.muryno.community.utils.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter : Callback<BaseData<User>> {


    var callback:LoginView?=null

    constructor(callback: LoginView?) {
        this.callback = callback
    }

    fun validation(email:String,passwrd:String){

        callback?.loadingStart()
        if(email.isEmpty()){
            callback?.emailError("Email cannot be empty!!")
            callback?.loadingFailed("")
            return
        }
        if(passwrd.isEmpty()){
            callback?.emailError("Password cannot be empty!!")
            callback?.loadingFailed("")
            return
        }


        RetrofitClient().getApi().login(email,passwrd).enqueue(this)
    }
    override fun onFailure(call: Call<BaseData<User>>, t: Throwable) {
        Log("Please check your connection and try again!")
        callback?.loadingFailed("Please check your connection and try again!")      }

    override fun onResponse(call: Call<BaseData<User>>, response: Response<BaseData<User>>) {
        if(response.isSuccessful){
            if(!response.body()?.error!!){
                response.body()!!.data?.let { StorePreferences().getInstance()?.putUser(it) }
                callback?.loadingSuccessful("Registration Successful !!")
            }else{
                callback?.loadingFailed(response.message())
            }
        }    }

}