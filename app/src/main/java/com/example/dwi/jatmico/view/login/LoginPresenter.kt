package com.example.dwi.jatmico.view.login

import com.example.dwi.jatmico.view.login.LoginView

interface LoginPresenter {
    fun initView(view: LoginView)
    fun exchangeToken(accessToken: String?)
}