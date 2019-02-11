package com.example.dwi.jatmico.view.detail_isues

import com.example.dwi.jatmico.data.models.Detail


interface DetailIssueView {
    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(e: Throwable)
    fun showingData(detail: Detail?)
    fun onSuccess()
//    fun alert(message: String)

}