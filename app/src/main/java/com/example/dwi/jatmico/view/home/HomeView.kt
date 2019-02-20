package com.example.dwi.jatmico.view.home

import com.example.dwi.jatmico.data.models.Me
import com.example.dwi.jatmico.data.models.Project

interface HomeView {
    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun showData(projects: MutableList<Project>)
    fun showingData(me: Me?)
    fun saveId(it: Me?)


}