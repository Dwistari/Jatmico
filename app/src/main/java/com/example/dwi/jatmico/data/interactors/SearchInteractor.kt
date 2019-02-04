package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.SearchResponse
import io.reactivex.Observable

class SearchInteractor {
    fun getSearch ( keyword : String, project_id: Int, page : Int, per_page : Int, token: String ): Observable<SearchResponse> {
        return api.getSearch (token , keyword ,project_id, page , per_page)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}
