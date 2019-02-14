package com.example.dwi.jatmico.view.detail_isues

import com.example.dwi.jatmico.data.interactors.DeleteInteractor
import com.example.dwi.jatmico.data.interactors.DetailInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailIssuePresenterImp : DetailIssuePresenter {

    private lateinit var view: DetailIssueView
    private var interactor: DetailInteractor = DetailInteractor()
    private var interactors: DeleteInteractor = DeleteInteractor()
    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun getDetail(isues_id: Int, token: String) {
        view.showLoading()
        interactor.getDetail(isues_id, token)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view.dismissLoading()
                    view.showingData(it.detail)
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

    override fun delIssues (token: String, issueId: String ) {
        view.showLoading()
        interactors.delIssues (token, issueId )
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view.dismissLoading()
                    view.onSuccess()
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

    override fun initView(view: DetailIssueView) {
        this.view = view
    }
}