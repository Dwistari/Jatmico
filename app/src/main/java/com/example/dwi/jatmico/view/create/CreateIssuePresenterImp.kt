package com.example.dwi.jatmico.view.create

import com.example.dwi.jatmico.data.interactors.CreateInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateIssuePresenterImp : CreateIssuePresenter {
    private lateinit var issueView: CreateIssueView
    private var interactor: CreateInteractor = CreateInteractor()
    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun initView(issueView: CreateIssueView) {
        this.issueView = issueView
    }

    override fun postIssues(
        project_id: RequestBody,
        title: RequestBody,
        description: RequestBody,
        severity_id: RequestBody,
        link: RequestBody,
        image: MultipartBody.Part,
        token: RequestBody
    ) {
        issueView.showLoading()
        interactor.postIssues(token, project_id, title, description, severity_id, link, image)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    issueView.dismissLoading()
                },
                {
                    issueView.showErrorAlert(it)
                    issueView.dismissLoading()
                }
            )?.let {
                disposables.add(
                    it
            )
            }
    }

    override fun getProjects(page: Int, perPage: Int, token: String) {
        issueView.showLoading()
        interactor.getCreate(page, perPage, token)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    issueView.dismissLoading()
                    issueView.showData(it.projects)
                },
                {
                    issueView.showErrorAlert(it)
                    issueView.dismissLoading()
                }
            )?.let {
                disposables.add(
                    it
            )
            }
    }

}

