package com.example.dwi.jatmico.data.interactors

import com.example.dwi.jatmico.api.ApiServiceInterface
import io.reactivex.Observable
import okhttp3.ResponseBody

class DeleteInteractor {

    fun delIssues ( isues_id: String, token: String ): Observable<ResponseBody> {
        return api.delIssues (isues_id, token )
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}
