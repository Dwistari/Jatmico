package com.example.dwi.jatmico.view.isues

import com.example.dwi.jatmico.data.models.Issues
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.data.models.Severity

interface IssuesView {
    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun showData(issues: MutableList<Issues>)
    fun addData(issues: MutableList<Issues>)
    fun showSeverity(severities: MutableList<Severity>)
    fun showingProject(projects: MutableList<Project>)
    fun showEmptyAlert()
    fun dataEnd()
}