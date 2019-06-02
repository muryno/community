package com.muryno.community.ui.presenter

import com.muryno.community.MainApplication
import com.muryno.community.db.BaseData
import com.muryno.community.db.model.User
import com.muryno.community.server.RetrofitClient
import com.muryno.community.ui.interfaces.IImageCompressionTaskListener
import com.muryno.community.ui.view.RegisterView
import com.muryno.community.utils.ImageCompressionTask
import com.muryno.community.utils.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.util.*

class RegisterPresenter: IImageCompressionTaskListener, retrofit2.Callback<BaseData<User>> {

    private val REQUEST_SELECT_PHOTO = 101

    var callback:RegisterView?=null

    constructor(callback: RegisterView?) {
        this.callback = callback
    }

    fun processImage(path: String) {
        val strings = ArrayList<String>()
        strings.add(path)
        MainApplication.getExecutorService()
            .execute(ImageCompressionTask(MainApplication.getInstance(), this, strings, REQUEST_SELECT_PHOTO))
    }

    fun validateInput(fname:String, lname:String, phone:String, emai:String, passwrd:String, photo: File?){


        callback!!.loadingStart()


        if(fname.isEmpty()){
            callback?.firstnameError("First name cannot be  empty!!")
            callback?.loadingFailed("")
            return
        }

        if(lname.isEmpty()){
            callback?.lastnameError("Last name cannot be  empty!!")
            callback?.loadingFailed("")
            return
        }

        if(phone.isEmpty()){
            callback?.phoneError("Phone number cannot be  empty!!")
            callback?.loadingFailed("")
            return
        }

        if(emai.isEmpty()){
            callback?.emailError("Email cannot be  empty!!")
            callback?.loadingFailed("")
            return
        }
        if(passwrd.isEmpty()){
            callback?.passError("Password cannot be  empty!!")
            callback?.loadingFailed("")
            return
        }
        if(!photo?.exists()!!){
            callback?.loadingFailed("Profile picture cannot be empty !!")
            return
        }

        val reqFil = RequestBody.create(MediaType.parse("multipart/form-data"), photo)
        val imag = MultipartBody.Part.createFormData("image", photo.name, reqFil)


        val fnam = RequestBody.create(MediaType.parse("multipart/form-data"), fname)
        val lnam = RequestBody.create(MediaType.parse("multipart/form-data"), lname)
        val phon = RequestBody.create(MediaType.parse("multipart/form-data"), phone)
        val email = RequestBody.create(MediaType.parse("multipart/form-data"), emai)
        val passwr = RequestBody.create(MediaType.parse("multipart/form-data"), passwrd)

        RetrofitClient().getApi().register(fnam,lnam,phon,email,passwr,imag).enqueue(this)
    }


    override fun onFailure(call: Call<BaseData<User>>, t: Throwable) {
        Log("Please check your connection and try again!")
        callback?.loadingFailed("Please check your connection and try again!")    }

    override fun onResponse(call: Call<BaseData<User>>, response: Response<BaseData<User>>) {
        if(response.isSuccessful){
            if(!response.body()?.error!!){
                callback?.loadingSuccessful("Registration Successful !!")
            }else{
                callback?.loadingFailed(response.message())
            }
        }
    }

    override fun onCompressed(file: List<File>, id: Int) {
        if (file.isEmpty())
            return
        callback?.updateImage(file[0])
    }

    override fun onError(throwable: Throwable) {

    }

}