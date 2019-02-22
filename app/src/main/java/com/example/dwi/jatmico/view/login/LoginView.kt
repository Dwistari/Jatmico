package com.example.dwi.jatmico.view.login

import com.example.dwi.jatmico.data.models.Token

interface LoginView {
    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(throwable: Throwable?)
    fun onSuccessLogin()

}