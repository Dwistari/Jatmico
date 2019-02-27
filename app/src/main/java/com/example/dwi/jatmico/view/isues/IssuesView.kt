package com.example.dwi.jatmico.view.isues

import com.example.dwi.jatmico.data.models.Isues
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.data.models.Severity

interface IssuesView {
    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun onLoadMore()
    fun showData(isues: MutableList<Isues>)
    fun showSeverity(severities: MutableList<Severity>)
    fun showingProject(projects: MutableList<Project>)
}