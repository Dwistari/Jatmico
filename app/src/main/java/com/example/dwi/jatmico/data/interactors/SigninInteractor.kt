package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.Token
import io.reactivex.Observable

class SigninInteractor() {
    fun getSignin(token: String): Observable<Token> {
        return api.exchangeToken(token)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}