package com.example.dwi.jatmico.view.create

import com.example.dwi.jatmico.data.models.Project


interface CreateView {

    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun showData(projects: MutableList<Project>)
    fun onSuccess()

}