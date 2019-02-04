package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.view.search.SearchView
import com.example.dwi.jatmico.data.interactors.SearchInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchPresenterImp : SearchPresenter {

    override fun getSearch(keyword: String, project_id: Int, page: Int, per_page: Int, token: String) {
        view?.showLoading()
        interactor?.getSearch(keyword, project_id, page, per_page, token)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view?.dismissLoading()
                    view?.showData(it.search)
                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }

    private var view: SearchView? = null
    private var interactor: SearchInteractor? = SearchInteractor()


    override fun initView(view : SearchView) {
        this.view = view
    }
}