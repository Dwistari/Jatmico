package com.example.dwi.jatmico.data.interactors

import android.content.SharedPreferences
import com.example.dwi.jatmico.data.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.MeResponse
import io.reactivex.Observable

class MeInteractor(private val sharedPreferences: SharedPreferences) {

    fun getMe(): Observable<MeResponse> {
        return api.getMe(sharedPreferences.getString("access_token", ""))
            .doOnNext {
                sharedPreferences.edit().let { sp ->
                    sp.putInt("user_id", it?.me?.id!!)
                    sp.apply()
                }
            }
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}