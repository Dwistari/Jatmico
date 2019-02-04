package com.example.dwi.jatmico.presenter

import com.example.dwi.jatmico.data.interactors.SigninInteractor
import com.example.dwi.jatmico.view.login.LoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SigninPresenterImp : SigninPresenter {

    override fun exchangeToken(accessToken: String?) {
        view?.showLoading()
        interactor?.getSignin(accessToken!!)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    view?.dismissLoading()
                    view?.saveToken(it)
                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }

    private var view: LoginView? = null
    private var interactor: SigninInteractor? =
        SigninInteractor()

    override fun initView(view: LoginView) {
        this.view = view
      }


}
