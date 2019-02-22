package com.example.dwi.jatmico.data.interactors

import android.content.SharedPreferences
import com.example.dwi.jatmico.data.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.IsuesResponse
import io.reactivex.Observable

class IsuesInteractor(private val sharedPreferences: SharedPreferences) {
    fun getIsues ( project_id: Int, page : Int, per_page : Int): Observable<IsuesResponse> {
        return api.getIssues (sharedPreferences.getString("access_token", "") ,project_id, page , per_page)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}
