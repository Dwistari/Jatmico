package com.example.dwi.jatmico.view.login

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dwi.jatmico.presenter.SigninPresenter
import com.example.dwi.jatmico.presenter.SigninPresenterImp
import com.example.dwi.jatmico.Constants
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.models.Token
import com.example.dwi.jatmico.view.home.HomeActivity
import com.extrainteger.identitaslogin.SymbolicConfig
import com.extrainteger.identitaslogin.SymbolicScope
import kotlinx.android.synthetic.main.signin.*
import com.extrainteger.identitaslogin.models.AuthToken
import com.extrainteger.identitaslogin.Callback
import com.extrainteger.identitaslogin.SymbolicException
import org.jetbrains.annotations.NotNull


class LoginActivity : AppCompatActivity(), LoginView {

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }


    override fun showErrorAlert(throwable: Throwable?) {
        Log.e("MainActivity", throwable?.localizedMessage)
        Toast.makeText(this@LoginActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun saveToken(it: Token) {
        Log.d("accesstoken", it.access_token)
        getSharedPreferences("Jatmico", MODE_PRIVATE).edit().let {sp ->
            sp.putString(getString(R.string.access_token), it.access_token)
            sp.putString(getString(R.string.refresh_token), it.refresh_token)
            sp.putBoolean("login", true)
            sp.apply()
        }
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private var presenter: SigninPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)
        presenter = SigninPresenterImp()
        presenter?.initView(this)

        val isLogin = getSharedPreferences("Jatmico", MODE_PRIVATE).getBoolean("login", false)
        if (isLogin) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
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


