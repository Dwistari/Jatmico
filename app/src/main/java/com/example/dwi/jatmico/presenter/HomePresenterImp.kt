package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.data.interactors.MeInteractor
import com.example.dwi.jatmico.data.interactors.ProjectInteractor
import com.example.dwi.jatmico.view.home.HomeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomePresenterImp : HomePresenter {

    override fun getMe(token: String) {
        view?.showLoading()
        interactors?.getMe(token)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view?.dismissLoading()
                    view?.showingData(it.me)
                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }

    override fun getProjects(page: Int, perPage: Int, token: String) {
        view?.showLoading()
        interactor?.getHome(page, perPage, token)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view?.dismissLoading()
                    view?.showData(it.projects)
                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }
    private var view: HomeView? = null
    private var interactor: ProjectInteractor? = ProjectInteractor()
    private var interactors : MeInteractor? = MeInteractor()

    override fun initView(view: HomeView) {
        this.view = view
    }

}