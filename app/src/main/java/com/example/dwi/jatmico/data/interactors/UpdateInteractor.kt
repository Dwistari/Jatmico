package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.data.api.ApiServiceInterface
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class UpdateInteractor {

    fun updateIssues (token: String, id : Int, project_id: RequestBody, title: RequestBody, description: RequestBody,
                    severity_id: RequestBody, link: RequestBody, image: MultipartBody.Part?): Observable<ResponseBody> {
        return api.updateIssues(id, project_id, title, description, severity_id, link, image,token)
    }


    private var api: ApiServiceInterface = ApiServiceInterface.create()
}