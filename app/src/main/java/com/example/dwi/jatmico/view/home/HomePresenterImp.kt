package com.example.dwi.jatmico.view.home

import android.content.SharedPreferences
import com.example.dwi.jatmico.data.interactors.MeInteractor
import com.example.dwi.jatmico.data.interactors.ProjectInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenterImp(private var sharedPreferences: SharedPreferences) : HomePresenter {
    private lateinit var view: HomeView
    private var interactor: ProjectInteractor = ProjectInteractor(sharedPreferences)
    private var interactors: MeInteractor = MeInteractor(sharedPreferences)
    private var disposables = CompositeDisposable()

    override fun getMe() {
        view.showLoading()
        interactors.getMe()
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view.dismissLoading()
                    view.showingData(it.me)
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

    override fun getProjects(page: Int, perPage: Int) {
        view.showLoading()
        interactor.getProjects(page, perPage)
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