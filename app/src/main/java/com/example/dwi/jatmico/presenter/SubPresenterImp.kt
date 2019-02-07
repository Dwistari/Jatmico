package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.data.interactors.IsuesInteractor
import com.example.dwi.jatmico.data.interactors.ProjectInteractor
import com.example.dwi.jatmico.data.interactors.SeverityInteractor
import com.example.dwi.jatmico.data.interactors.SubInteractor
import com.example.dwi.jatmico.view.my_submission.SubView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SubPresenterImp : SubPresenter {


    override fun getIsues(project_id: Int , page : Int, per_page : Int, token: String) {
        view?.showLoading()
        interactorr?.getIsues(project_id, page , per_page, token)
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
    override fun getSub ( sortBy : String, sortSeverity :Int, page : Int,per_page : Int, token: String) {
        view?.showLoading()
        interactor?.getSub( sortBy, sortSeverity,  page , per_page,token)
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

    override fun getProjects(page: Int, perPage: Int, token: String) {
        view?.showLoading()
        interactors?.getHome(page, perPage, token)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view?.dismissLoading()
                    if(it.projects.size !=0){
                        view?.showsData(it.projects)
                    }

                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }

    override fun getSeverity(token: String) {
        view?.showLoading()
        interactorry?.getSeverity( token)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view?.dismissLoading()
                    view?.showSeverity(it.severity)
                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }
    private var view: SubView? = null
    private var interactor: SubInteractor? = SubInteractor()
    private var interactors: ProjectInteractor? = ProjectInteractor()
    private var interactorr: IsuesInteractor? = IsuesInteractor()
    private var interactorry: SeverityInteractor? = SeverityInteractor()


    override fun initView(view: SubView) {
        this.view = view
    }
}