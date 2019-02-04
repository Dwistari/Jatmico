package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.IsuesResponse
import io.reactivex.Observable

class IsuesInteractor {
    fun getIsues ( project_id: Int, page : Int, per_page : Int, token: String ): Observable<IsuesResponse> {
        return api.getIssues (token ,project_id, page , per_page)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}
