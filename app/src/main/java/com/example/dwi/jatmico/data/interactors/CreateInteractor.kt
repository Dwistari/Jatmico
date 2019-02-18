package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.data.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.IsuesResponse
import com.example.dwi.jatmico.data.models.ProjectResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class CreateInteractor {

    fun postIssues (token: RequestBody, project_id: RequestBody, title: RequestBody, description: RequestBody,
                    severity_id: RequestBody, link: RequestBody, image: MultipartBody.Part): Observable<ResponseBody> {
        return api.postIssues(token, project_id, title, description, severity_id, link, image)
    }
    fun getCreate(page: Int, perPage: Int, token: String): Observable<ProjectResponse> {
        return api.getProjects(token,page,perPage)
    }
    fun getIsues ( project_id: Int, page : Int, per_page : Int, token: String ): Observable<IsuesResponse> {
        return api.getIssues (token ,project_id, page , per_page)
    }


    private var api: ApiServiceInterface = ApiServiceInterface.create()
}

