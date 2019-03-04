package com.example.dwi.jatmico.view.my_submission

import android.content.SharedPreferences
import com.example.dwi.jatmico.data.interactors.IsuesInteractor
import com.example.dwi.jatmico.data.interactors.ProjectInteractor
import com.example.dwi.jatmico.data.interactors.SeverityInteractor
import com.example.dwi.jatmico.data.interactors.SubInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MySubmissionPresenterImp(private val sharedPreferences: SharedPreferences) : MySubmissionPresenter {
    private lateinit var view: MySubmissionView
    private var interactor: SubInteractor = SubInteractor(sharedPreferences)
    private var interactors: ProjectInteractor = ProjectInteractor(sharedPreferences)
    private var interactorry: SeverityInteractor = SeverityInteractor(sharedPreferences)
    private var disposables = CompositeDisposable()



    override fun getSub (  page : Int,per_page : Int) {
        if (page == 1) {
            view.showLoading()
        }
        interactor.getSub( page , per_page)
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

    override fun getProjects(page: Int, perPage: Int) {
        view.showLoading()
        interactors.getProjects(page, perPage)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view.dismissLoading()
                    if(it.projects.size !=0){
                        view.showsProject(it.projects)
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
        view.showLoading()
        interactorry.getSeverity()
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view.dismissLoading()
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


    override fun initView(view: MySubmissionView) {
        this.view = view
    }
}