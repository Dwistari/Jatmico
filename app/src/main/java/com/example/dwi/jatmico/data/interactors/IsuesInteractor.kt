package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.data.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.IsuesResponse
import com.example.dwi.jatmico.data.models.ProjectResponse
import io.reactivex.Observable

class IsuesInteractor {
    fun getIsues ( project_id: Int, page : Int, per_page : Int, token: String ): Observable<IsuesResponse> {
        return api.getIssues (token ,project_id, page , per_page)
    }
    fun getProject (page: Int, perPage: Int, token: String?): Observable<ProjectResponse> {
        return api.getProjects(token,page,perPage)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}
