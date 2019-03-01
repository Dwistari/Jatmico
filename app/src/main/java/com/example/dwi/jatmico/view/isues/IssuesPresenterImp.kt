package com.example.dwi.jatmico.view.isues

import android.content.SharedPreferences
import com.example.dwi.jatmico.data.interactors.IsuesInteractor
import com.example.dwi.jatmico.data.interactors.SeverityInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class IssuesPresenterImp(
    private val sharedPreferences: SharedPreferences
) : IssuesPresenter {
    private lateinit var view: IssuesView
    private var interactor: IsuesInteractor = IsuesInteractor(sharedPreferences)
    private var interactorry: SeverityInteractor = SeverityInteractor(sharedPreferences)
    private var disposables = CompositeDisposable()


    override fun getIsues(project_id: Int, page: Int, per_page: Int) {
        if (page == 1) {
            view.showLoading()
        }
        interactor.getIsues(project_id, page, per_page)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    if (page == 1) {
                        view.dismissLoading()
                        if (it.isues.size != 0) {
                            if (it.isues.size >= per_page) {
                                view.showData(it.isues)
                            } else {
                                view.showData(it.isues)
                                view.dataEnd()
                            }
                        } else {
                            view.showEmptyAlert()
                        }
                    } else {
                        if (it.isues.size != 0) {
                            if (it.isues.size >= per_page) {
                                view.addData(it.isues)
                            } else {
                                view.addData(it.isues)
                                view.dataEnd()
                            }
                        } else {
                            view.dataEnd()
                        }
                    }
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

    override fun getSeverity() {
        interactorry.getSeverity()
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view.showSeverity(it.severity)
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
                    view.showingProject(it.projects)
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


    override fun initView(view: IssuesView) {
        this.view = view
    }
}