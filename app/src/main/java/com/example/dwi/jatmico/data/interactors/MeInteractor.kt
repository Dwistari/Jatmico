package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.MeResponse
import com.example.dwi.jatmico.data.models.ProjectResponse
import io.reactivex.Observable

class MeInteractor {

    fun getMe( token: String): Observable<MeResponse> {
        return api.getMe(token)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}