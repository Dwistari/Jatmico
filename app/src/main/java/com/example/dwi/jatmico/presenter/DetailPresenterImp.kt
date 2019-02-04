package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.data.interactors.DeleteInteractor
import com.example.dwi.jatmico.data.interactors.DetailInteractor
import com.example.dwi.jatmico.view.detail_isues.DetailView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class DetailPresenterImp : DetailPresenter {


    override fun getDetail(isues_id: Int, token: String) {
        view?.showLoading()
        interactor?.getDetail(isues_id, token)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view?.dismissLoading()
                    view?.showingData(it.detail)
                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }
//    override fun getDetails(sub_id: Int, token: String) {
//        view?.showLoading()
//        interactor?.getDetails(sub_id, token)
//            ?.subscribeOn(Schedulers.io())
//            ?.observeOn(AndroidSchedulers.mainThread())
//            ?.subscribe(
//                {
//                    view?.dismissLoading()
//                    view?.showingData(it.detail)
//                },
//                {
//                    view?.showErrorAlert(it)
//                    view?.dismissLoading()
//                }
//            )
//    }
    override fun delIssues (isuesId: String, token: String ) {
        view?.showLoading()
        interactors?.delIssues (isuesId, token )
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view?.dismissLoading()
                    view?.onSuccess()
                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }

    override fun delSub (isuesId: String, token: String ) {
        view?.showLoading()
        interactors?.delIssues (isuesId, token )
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view?.dismissLoading()
                    view?.onSuccess()
                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }




    private var view: DetailView? = null
    private var interactor: DetailInteractor? = DetailInteractor()
    private var interactors: DeleteInteractor? = DeleteInteractor()


    override fun initView(view: DetailView) {
        this.view = view
    }
}