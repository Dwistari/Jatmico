package com.example.dwi.jatmico.data.interactors

import android.content.SharedPreferences
import com.example.dwi.jatmico.data.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.SeverityResponse
import io.reactivex.Observable

class SeverityInteractor(private val sharedPreferences: SharedPreferences) {
    fun getSeverity (): Observable<SeverityResponse> {
        return api.getSeverity(sharedPreferences.getString("access_token", ""))
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}
