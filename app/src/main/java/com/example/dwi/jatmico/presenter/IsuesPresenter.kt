package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.view.isues.IsuesView

interface IsuesPresenter {
        fun initView(view: IsuesView)
        fun getIsues(project_id: Int, page : Int, per_page : Int, token: String)
}