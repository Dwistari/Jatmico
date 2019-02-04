package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.view.create.CreateView
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface CreatePresenter {

    fun initView(view: CreateView)
    fun postIssues (
        project_id: RequestBody,
        title: RequestBody,
        description: RequestBody,
        severity_id: RequestBody,
        link: RequestBody,
        image: MultipartBody.Part,
        token: RequestBody)

    fun getProjects (page: Int, perPage: Int, token: String)


}
