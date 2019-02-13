package com.example.dwi.jatmico.view.isues

import com.example.dwi.jatmico.data.interactors.IsuesInteractor
import com.example.dwi.jatmico.data.interactors.SeverityInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class IssuesPresenterImp : IssuesPresenter {
    private lateinit var view: IssuesView
    private var interactor: IsuesInteractor = IsuesInteractor()
    private var interactorry: SeverityInteractor = SeverityInteractor()
    private var disposables = CompositeDisposable()


    override fun getIsues(project_id: Int, page: Int, per_page: Int, token: String) {
        view.showLoading()
        interactor.getIsues(project_id, page, per_page, token)
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


    override fun initView(view: IssuesView) {
        this.view = view
    }
}