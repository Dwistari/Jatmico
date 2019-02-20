package com.example.dwi.jatmico.view.home

import com.example.dwi.jatmico.data.interactors.MeInteractor
import com.example.dwi.jatmico.data.interactors.ProjectInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenterImp : HomePresenter {
    private lateinit var view: HomeView
    private var interactor: ProjectInteractor = ProjectInteractor()
    private var interactors: MeInteractor = MeInteractor()
    private var disposables = CompositeDisposable()

    override fun getMe(token: String) {
        view.showLoading()
        interactors.getMe(token)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view.dismissLoading()
                    view.showingData(it.me)
                    view.saveId(it.me)
                },
                {
                    view.showErrorAlert(it)
                    view.dismissLoading()
                }
            )?.let {
                disposables.add(
                    it
                )
            }
    }

    override fun getProjects(page: Int, perPage: Int, token: String) {
        view.showLoading()
        interactor.getHome(page, perPage, token)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view.dismissLoading()
                    view.showData(it.projects)
                },
                {
                    view.showErrorAlert(it)
                    view.dismissLoading()
                }
            )?.let {
                disposables.add(
                    it
                )
            }
    }
    override fun initView(view: HomeView) {
        this.view = view
    }

    override fun detach() {
        disposables.clear()
    }
}