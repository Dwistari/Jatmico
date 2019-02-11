package com.example.dwi.jatmico.view.create

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface CreateIssuePresenter {

    fun initView(issueView: CreateIssueView)
    fun postIssues(
        project_id: RequestBody,
        title: RequestBody,
        description: RequestBody,
        severity_id: RequestBody,
        link: RequestBody,
        image: MultipartBody.Part,
        token: RequestBody
    )

    fun getProjects(page: Int, perPage: Int, token: String)

}
