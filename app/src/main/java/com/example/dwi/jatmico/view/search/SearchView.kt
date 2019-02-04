package com.example.dwi.jatmico.view.search

import com.example.dwi.jatmico.data.models.Search

interface SearchView {
    fun showLoading()
    fun dismissLoading()
    fun showErrorAlert(it: Throwable)
  //  fun showData(search: MutableList<Search>)
    fun showData(search: MutableList<Search>)
}