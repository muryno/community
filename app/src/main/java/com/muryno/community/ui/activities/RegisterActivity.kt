package com.muryno.community.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.muryno.community.BuildConfig
import com.muryno.community.R
import com.muryno.community.ui.base.BaseActivity
import com.muryno.community.ui.presenter.RegisterPresenter
import com.muryno.community.ui.view.RegisterView
import com.muryno.community.utils.getOutputMediaFile
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.password_edit_text
import java.io.File

class RegisterActivity : BaseActivity(), RegisterView {


    var photo: File? = null
           var path:File? = null
    private val CAMERA_INTENT_CODE = 102
    val REQUEST_PERMISSION_CODE = 103
    private val GALLERY_INTENT_CODE = 104

    private var presenter: RegisterPresenter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        presenter= RegisterPresenter(this)

        fab_.setOnClickListener { requestPermission() }

        btn_regr.setOnClickListener {


                presenter!!.validateInput(fname_edit_text.text.toString(),lname_edit_text.text.toString()
                    ,phone_edit_text.text.toString()
                    ,email_edit_text.text.toString()
                    ,password_edit_text.text.toString(),photo)
            }

    }



    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_INTENT_CODE)
    }
     private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            path = getOutputMediaFile()
            if (path != null) {
                val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", path!!)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivityForResult(takePictureIntent, CAMERA_INTENT_CODE)
            }
        }
    }


    private fun requestPermission() {
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_CODE
                )
            }
        } else {
            showPictureDialog()
        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePicture()
            }
        }
        pictureDialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GALLERY_INTENT_CODE ->
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY_INTENT_CODE)
                } else {
                    requestPermission()
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
            REQUEST_PERMISSION_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestPermission()
            } else {
                requestPermission()
            }
        }
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            return
        }
        if (requestCode == CAMERA_INTENT_CODE && resultCode == RESULT_OK) {
            if (path != null) {
                val path = this.path!!.path
                presenter?.processImage(path)
            }
        } else if (requestCode == GALLERY_INTENT_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                assert(selectedImage != null)
                val cursor = contentResolver.query(
                    selectedImage,
                    filePathColumn, null, null, null
                )!!
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val picturePath = cursor.getString(columnIndex)
                if (picturePath != null) {
                    path = File(picturePath)
                    presenter?.processImage(path!!.path)
                }
                cursor.close()
            }
        }
    }
    override fun firstnameError(msg: String) {

        fname_text_input.error = msg
    }

    override fun emailError(msg: String) {
        email_text_input.error = msg
    }

    override fun passError(msg: String) {
        password_text_input.error = msg
    }

    override fun updateImage(file: File) {
        photo = file
        Glide.with(this)
            .load(photo)
            .into(profileImage)    }

    override fun loadingStart() {
        btn_regr.startLoading()
    }

    override fun loadingFailed(msg: String?) {
        if (!msg?.isEmpty()!!)
            toastError(msg)
        btn_regr.loadingFailed()
    }

    override fun loadingSuccessful(msg: String?) {
        btn_regr.loadingSuccessful()
        if (!msg?.isEmpty()!!)
            toastSuccess(msg)
        startActivity(Intent(applicationContext,MainActivity::class.java))
        this.finish()

    }
    override fun lastnameError(msg: String) {
        lname_text_input.error=msg
    }

    override fun phoneError(msg: String) {
        phone_text_input.error=msg
    }



}
