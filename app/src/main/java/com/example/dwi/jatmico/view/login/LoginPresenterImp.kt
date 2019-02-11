package com.example.dwi.jatmico.view.login

import com.example.dwi.jatmico.data.interactors.SigninInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginPresenterImp : LoginPresenter {

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
