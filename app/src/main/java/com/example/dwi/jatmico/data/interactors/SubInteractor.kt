package com.example.dwi.jatmico.data.interactors

import android.content.SharedPreferences
import com.example.dwi.jatmico.data.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.IsuesResponse
import io.reactivex.Observable

class SubInteractor(private val sharedPreferences: SharedPreferences) {
    fun getSub (  page : Int,per_page : Int): Observable<IsuesResponse> {
        return api.getSub (sharedPreferences.getString("access_token", "") ,  page , per_page)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}
