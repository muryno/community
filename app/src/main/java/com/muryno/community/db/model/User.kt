package com.muryno.community.db.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User : Serializable {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    var id: Int?=null

    @SerializedName("fname")
    @Expose
    var fname: String? = null

    @SerializedName("lname")
    @Expose
    var lname: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null
}