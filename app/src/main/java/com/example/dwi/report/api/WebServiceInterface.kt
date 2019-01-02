package com.example.dwi.report.api

import com.example.dwi.report.model.SimpleGetModel
import retrofit2.http.GET
import java.util.*
import rx.Observable

interface WebServiceInterface {
    @GET("api/v1/pinger/pong/")
    fun getSampleData(): Observable <SimpleGetModel>

}