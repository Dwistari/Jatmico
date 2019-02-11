package com.example.dwi.jatmico.view.isues

import com.example.dwi.jatmico.data.models.Isues

interface IssuesView {
    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun showData(isues: MutableList<Isues>)
}