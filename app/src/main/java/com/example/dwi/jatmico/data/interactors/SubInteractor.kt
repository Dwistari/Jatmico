package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.IsuesResponse
import io.reactivex.Observable

class SubInteractor {
    fun getSub ( sortBy : String, sortSeverity :Int, page : Int,per_page : Int, token: String? ): Observable<IsuesResponse> {
        return api.getSub (token  ,sortBy, sortSeverity,  page , per_page)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}
