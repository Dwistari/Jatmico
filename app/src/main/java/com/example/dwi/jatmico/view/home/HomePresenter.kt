package com.example.dwi.jatmico.view.home


interface HomePresenter {
    fun initView(view: HomeView)
    fun getProjects(page: Int, perPage: Int)
    fun getMe()
    fun detach()
}