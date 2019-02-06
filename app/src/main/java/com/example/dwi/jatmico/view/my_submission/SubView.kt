package com.example.dwi.jatmico.view.my_submission

import com.example.dwi.jatmico.data.models.Isues
import com.example.dwi.jatmico.data.models.Project

interface SubView {
    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun showData(isues: MutableList<Isues>)
    fun showsData(projects: MutableList<Project>)
//    fun getSeverity()
}