package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.ProjectResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class CreateInteractor {

    fun postIssues (token: RequestBody, project_id: RequestBody, title: RequestBody, description: RequestBody, severity_id: RequestBody, link: RequestBody, image: MultipartBody.Part): Observable<ResponseBody> {
        return api.postIssues(token, project_id, title, description, severity_id, link, image)
    }
    fun getCreate(page: Int, perPage: Int, token: String): Observable<ProjectResponse> {
        return api.getProjects(token,page,perPage)
    }


    private var api: ApiServiceInterface = ApiServiceInterface.create()
}

