package com.muryno.community.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.muryno.community.R
import com.muryno.community.ui.base.BaseActivity
import com.muryno.community.ui.presenter.LoginPresenter
import com.muryno.community.ui.view.LoginView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginView {

    private var presenter:LoginPresenter?=null
            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
                presenter= LoginPresenter(this)

                btn_login.setOnClickListener {
                    presenter!!.validation(edt_email_login.text.toString(),password_edit_text.text.toString())
                }
                btn_register.setOnClickListener {
                    startActivity(Intent(applicationContext,RegisterActivity::class.java))
                }
    }

    override fun emailError(msg: String) {
        email_text_input?.error=msg
    }

    override fun passError(msg: String) {
        password_text_input?.error=msg
    }

    override fun loadingStart() {
        btn_login.startLoading()
    }

    override fun loadingFailed(msg: String?) {

        btn_login.loadingFailed()
        if (!msg?.isEmpty()!!)

            toastError(msg)
    }

    override fun loadingSuccessful(msg: String?) {
        btn_login.loadingSuccessful()
        if (!msg?.isEmpty()!!)
            toastSuccess(msg)

        startActivity(Intent(applicationContext,MainActivity::class.java))
        this.finish()
    }

}
