package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.data.interactors.CreateInteractor
import com.example.dwi.jatmico.view.create.CreateView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreatePresenterImp : CreatePresenter {
    override fun postIssues(
        project_id: RequestBody,
        title: RequestBody,
        description: RequestBody,
        severity_id: RequestBody,
        link: RequestBody,
        image: MultipartBody.Part,
        token: RequestBody
    ) {
    

        view?.showLoading()
        interactor?.postIssues(token, project_id, title, description, severity_id, link, image)

            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view?.dismissLoading()
                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }
    override fun getProjects(page: Int, perPage: Int, token: String) {
        view?.showLoading()
        interactor?.getCreate(page, perPage, token)
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

    private var view: CreateView? = null
    private var interactor: CreateInteractor? = CreateInteractor()


    override fun initView(view: CreateView) {
        this.view = view
    }

}

