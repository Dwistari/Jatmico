package com.example.dwi.jatmico.view.detail_isues

interface DetailIssuePresenter {
    fun initView(view: DetailIssueView)
    fun getDetail(isues_id: Int, token: String)
    fun delIssues(token: String, issueId: String)
}