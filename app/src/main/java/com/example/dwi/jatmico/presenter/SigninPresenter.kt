package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.view.login.LoginView

interface SigninPresenter {
    fun initView(view: LoginView)
    fun exchangeToken(accessToken: String?)
}