package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.data.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.DetailResponse
import com.example.dwi.jatmico.data.models.MeResponse
import io.reactivex.Observable

class DetailInteractor {

    fun getDetail ( isues_id: Int, token: String ): Observable<DetailResponse> {
        return api.getDetail (isues_id ,token)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}
