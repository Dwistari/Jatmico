package com.example.dwi.jatmico.view.login

import android.content.SharedPreferences
import com.example.dwi.jatmico.data.interactors.LoginInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginPresenterImp(sharedPreferences: SharedPreferences) : LoginPresenter {
    private var view: LoginView? = null
    private var interactor: LoginInteractor =
        LoginInteractor(sharedPreferences)
    private var io = Schedulers.io()
    private var mainThread = AndroidSchedulers.mainThread()

    override fun exchangeToken(accessToken: String?) {
        view?.showLoading()
        interactor.getSignin(accessToken!!)
            .subscribeOn(io)
            .observeOn(mainThread)
            .subscribe(
                {
                    interactor.saveToken(it)
                        .subscribeOn(io)
                        .observeOn(mainThread)
                        .subscribe(
                            {
                                view?.onSuccessLogin()
                            },
                            {
                                view?.showErrorAlert(it)
                            }
                        )
                    view?.dismissLoading()
                },
                {
                    view?.showErrorAlert(it)
                    view?.dismissLoading()
                }
            )
    }

    override fun initView(view: LoginView) {
        this.view = view
      }
}
