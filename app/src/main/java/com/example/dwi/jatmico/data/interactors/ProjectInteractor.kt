package com.example.dwi.jatmico.data.interactors

import android.content.SharedPreferences
import com.example.dwi.jatmico.data.api.ApiServiceInterface
import com.example.dwi.jatmico.data.models.ProjectResponse
import io.reactivex.Observable


class ProjectInteractor(private val sharedPreferences: SharedPreferences) {
    fun getProjects(page: Int, perPage: Int): Observable<ProjectResponse> {
        return api.getProjects(sharedPreferences.getString("access_token", ""), page, perPage)
    }

    private var api: ApiServiceInterface = ApiServiceInterface.create()
}