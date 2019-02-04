package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.view.search.SearchView

interface SearchPresenter {
    fun initView(view: SearchView)
    fun getSearch (keyword: String, project_id: Int, page : Int, per_page : Int, token: String)
}