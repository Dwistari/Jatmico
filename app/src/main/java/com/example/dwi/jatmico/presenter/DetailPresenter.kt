package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.view.detail_isues.DetailView

interface DetailPresenter {
    fun initView(view: DetailView)
    fun getDetail(isues_id: Int, token: String)
//    fun getDetails(sub_id: Int, token: String)
    fun delIssues(token: String, isues_Id: String )
    fun delSub(token: String, isues_Id: String )
}