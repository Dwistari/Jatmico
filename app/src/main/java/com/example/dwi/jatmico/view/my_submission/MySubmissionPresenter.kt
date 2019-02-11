package com.example.dwi.jatmico.view.my_submission

interface MySubmissionPresenter {
    fun initView(view: MySubmissionView)
//    fun getSub ( sortBy : String, sortSeverity :Int, page : Int,per_page : Int, token: String)
    fun getSub (  page : Int,per_page : Int, token: String)
    fun getProjects(page: Int, perPage: Int, token: String)
    fun getIsues(project_id: Int, page : Int, per_page : Int, token: String)
    fun getSeverity ( token: String)
}