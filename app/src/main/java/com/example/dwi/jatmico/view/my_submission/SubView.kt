package com.example.dwi.jatmico.view.my_submission

import com.example.dwi.jatmico.data.models.Isues
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.data.models.Severitys

interface SubView {
    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun showData(submission: MutableList<Isues>)
    fun showsProject(projects: MutableList<Project>)
    fun showSeverity(severities: MutableList<Severitys>)
}