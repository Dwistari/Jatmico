package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.view.my_submission.SubView

interface SubPresenter {
    fun initView(view: SubView)
    fun getSub ( page : Int, per_page : Int, token: String)
    fun getProjects(page: Int, perPage: Int, token: String)
}