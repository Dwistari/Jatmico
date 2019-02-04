package com.example.dwi.jatmico.view.detail_isues

import com.example.dwi.jatmico.data.models.Detail


interface DetailView {
    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun showingData(detail: Detail?)
    fun onSuccess()
//    fun alert(message: String)

}