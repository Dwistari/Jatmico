package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.view.my_submission.SubView

interface SubPresenter {
    fun initView(view: SubView)
    fun getSub ( sortBy : String, sortSeverity :Int, page : Int,per_page : Int, token: String)
    fun getProjects(page: Int, perPage: Int, token: String)
    fun getIsues(project_id: Int, page : Int, per_page : Int, token: String)
    fun getSeverity ( token: String)
}