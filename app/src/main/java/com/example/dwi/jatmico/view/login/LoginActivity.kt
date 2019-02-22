package com.example.dwi.jatmico.view.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.dwi.jatmico.utils.Constants
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.view.BaseActivity
import com.example.dwi.jatmico.view.home.HomeActivity
import com.extrainteger.identitaslogin.SymbolicConfig
import com.extrainteger.identitaslogin.SymbolicScope
import kotlinx.android.synthetic.main.activity_login.*
import com.extrainteger.identitaslogin.models.AuthToken
import com.extrainteger.identitaslogin.Callback
import com.extrainteger.identitaslogin.SymbolicException
import org.jetbrains.annotations.NotNull


class LoginActivity : BaseActivity(), LoginView {
    private var presenter: LoginPresenter? = null

    override fun dismissLoading() {
        showProgressDialog()
    }

    override fun showLoading() {
        hideProgressDialog()
    }


    override fun showErrorAlert(throwable: Throwable?) {
        Log.e("MainActivity", throwable?.localizedMessage)
        Toast.makeText(this@LoginActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessLogin() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun getLayout(): Int {
        return R.layout.activity_login
    }

    override fun onViewReady() {
        presenter = LoginPresenterImp(getSharedPreferences("Jatmico", MODE_PRIVATE))
        presenter?.initView(this)

        val isLogin = getSharedPreferences("Jatmico", MODE_PRIVATE).getBoolean("login", false)
        if (isLogin) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            val scopes: MutableList<String> = ArrayList() //you can leave it with empty data
            scopes.add(SymbolicScope.PUBLIC) // this addition just an example

            val config = SymbolicConfig(
                this@LoginActivity,
                Constants.base_url,
                Constants.client_id,
                Constants.client_secret,
                Constants.redirect_uri,
                scopes,
                Constants.app_referer
            )

            login_button.configure(config)


            login_button.setCallback(object : Callback<AuthToken>() {
                override fun success(result: com.extrainteger.identitaslogin.Result<AuthToken>) {
                    presenter?.exchangeToken(result.data.accessToken)
                }

                override fun failure(@NotNull exception: SymbolicException) {
                    //do some handle when this login got error
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        login_button.onActivityResult(requestCode, resultCode, data);
    }
}


