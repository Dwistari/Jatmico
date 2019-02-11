package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.data.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.SeverityResponse
import io.reactivex.Observable

class SeverityInteractor {
    fun getSeverity (token: String ): Observable<SeverityResponse> {
        return api.getSeverity(token)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}
