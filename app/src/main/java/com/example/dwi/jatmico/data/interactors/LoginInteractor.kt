package com.example.dwi.jatmico.data.interactors

import android.content.SharedPreferences
import com.example.dwi.jatmico.R
import com.example.dwi.jatmico.data.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.Token
import io.reactivex.Completable
import io.reactivex.Observable

class LoginInteractor(private var sharedPreferences: SharedPreferences) {
    fun getSignin(token: String): Observable<Token> {
        return api.exchangeToken(token)
    }

    fun saveToken(token: Token): Completable {
        return Completable.fromCallable {
            sharedPreferences.edit().let {sp ->
                sp.putString("access_token", token.access_token)
                sp.putString("refresh_token", token.refresh_token)
                sp.putBoolean("login", true)
                sp.apply()
            }
        }
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}