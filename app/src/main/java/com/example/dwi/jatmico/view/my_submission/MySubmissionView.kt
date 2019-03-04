package com.example.dwi.jatmico.view.my_submission

import com.example.dwi.jatmico.data.models.Issues
import com.example.dwi.jatmico.data.models.Project
import com.example.dwi.jatmico.data.models.Severity

interface MySubmissionView {
    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
    fun showData(submission: MutableList<Issues>)
    fun showsProject(projects: MutableList<Project>)
    fun showSeverity(severities: MutableList<Severity>)
    fun addData(submission: MutableList<Issues>)
    fun showEmptyAlert()
    fun dataEnd()
}