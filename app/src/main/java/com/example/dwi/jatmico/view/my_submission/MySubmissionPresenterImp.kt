package com.example.dwi.jatmico.view.my_submission

import com.example.dwi.jatmico.data.interactors.IsuesInteractor
import com.example.dwi.jatmico.data.interactors.ProjectInteractor
import com.example.dwi.jatmico.data.interactors.SeverityInteractor
import com.example.dwi.jatmico.data.interactors.SubInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MySubmissionPresenterImp : MySubmissionPresenter {
    private lateinit var view: MySubmissionView
    private var interactor: SubInteractor = SubInteractor()
    private var interactors: ProjectInteractor = ProjectInteractor()
    private var interactorr: IsuesInteractor = IsuesInteractor()
    private var interactorry: SeverityInteractor = SeverityInteractor()
    private var disposables = CompositeDisposable()


    override fun getIsues(project_id: Int , page : Int, per_page : Int, token: String) {
        view.showLoading()
        interactorr.getIsues(project_id, page , per_page, token)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view.dismissLoading()
                    view.showData(it.isues)
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
    override fun getSub (  page : Int,per_page : Int, token: String) {
        view.showLoading()
        interactor.getSub( page , per_page,token)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view.dismissLoading()
                    view.showData(it.isues)
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
        interactors.getHome(page, perPage, token)
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

    override fun getSeverity(token: String) {
        view.showLoading()
        interactorry.getSeverity( token)
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