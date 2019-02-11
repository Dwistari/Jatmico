package com.example.dwi.jatmico.view.isues

interface IssuesPresenter {
        fun initView(view: IssuesView)
        fun getIsues(project_id: Int, page : Int, per_page : Int, token: String)
}