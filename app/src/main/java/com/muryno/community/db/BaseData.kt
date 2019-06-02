package com.muryno.community.db

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BaseData<T> {
    @SerializedName("error")
    @Expose
     var error: Boolean = false

    @SerializedName("message")
    @Expose
     var message = ""

    @SerializedName("data")
    @Expose
     var data: T? = null


}