package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.view.home.HomeView


interface HomePresenter {
    fun initView(view: HomeView)
    fun getProjects(page: Int, perPage: Int, token: String)
    fun getMe( token: String)
}