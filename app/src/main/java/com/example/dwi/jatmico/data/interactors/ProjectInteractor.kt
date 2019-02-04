package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.ProjectResponse
import io.reactivex.Observable


class ProjectInteractor {
    fun getHome(page: Int, perPage: Int, token: String?): Observable<ProjectResponse> {
        return api.getProjects(token,page,perPage)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}