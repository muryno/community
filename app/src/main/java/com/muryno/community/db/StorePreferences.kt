package com.muryno.community.db

import android.content.Intent
import android.content.SharedPreferences
import com.google.gson.Gson
import com.muryno.community.MainApplication
import com.muryno.community.db.model.User
import com.muryno.community.ui.activities.LoginActivity

class StorePreferences {
    private var sInstance:StorePreferences?=null
    private  var mSharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor? = null
    private val PREF_NAME = "truckit_user_app"
    private val PREF_MODE = 0
    private val KEY_USER = "user"


    constructor(){
        mSharedPreferences = MainApplication.getInstance().applicationContext
            .getSharedPreferences(PREF_NAME, PREF_MODE)
        editor= mSharedPreferences.edit()
    }




    @Synchronized
    fun getInstance(): StorePreferences? {
        if (sInstance == null) sInstance = StorePreferences()
        return sInstance
    }

    fun putUser(user:User){
        val gson = Gson()
        val json = gson.toJson(user)
        editor?.putString(KEY_USER, json)
        editor?.commit()
    }

    fun getUser(): User?{

        if (mSharedPreferences.getString(KEY_USER, null) != null) {
            val gson = Gson()
            val json = mSharedPreferences.getString(KEY_USER, null)
            return gson.fromJson(json, User::class.java)
        }
        return null
    }

    fun signOut() {
        editor?.clear()?.apply()
        val intent = Intent(MainApplication.getInstance().applicationContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        MainApplication.getInstance().startActivity(intent)
    }
}