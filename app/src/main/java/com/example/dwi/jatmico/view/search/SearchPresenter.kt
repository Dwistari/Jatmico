package com.example.dwi.jatmico.view.search


interface SearchPresenter {
    fun initView(view: SearchView)
    fun getSearch (keyword: String, project_id: Int, page : Int, per_page : Int, token: String)
    fun getSearchSub (keyword: String, page : Int, per_page : Int, token: String)
}