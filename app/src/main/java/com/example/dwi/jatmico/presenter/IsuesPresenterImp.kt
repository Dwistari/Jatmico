package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.data.interactors.IsuesInteractor
import com.example.dwi.jatmico.view.isues.IsuesView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class IsuesPresenterImp : IsuesPresenter {


    override fun getIsues(project_id: Int , page : Int, per_page : Int, token: String) {
        view?.showLoading()
        interactor?.getIsues(project_id, page , per_page, token)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view?.dismissLoading()
                    view?.showData(it.isues)
                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }
    private var view: IsuesView? = null
    private var interactor: IsuesInteractor? = IsuesInteractor()


    override fun initView(view: IsuesView) {
        this.view = view
    }
}