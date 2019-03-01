package com.example.dwi.jatmico.view.create

import com.example.dwi.jatmico.data.models.Issues
import com.example.dwi.jatmico.data.models.Project


interface CreateIssueView {

    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun showData(projects: MutableList<Project>)
    fun showDataIssue(isues: MutableList<Issues>)

}