package com.example.dwi.jatmico.api

import com.example.dwi.jatmico.Constants
import com.example.dwi.jatmico.data.models.*
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiServiceInterface {
    @GET("api/v1/users/callback")
    fun exchangeToken(@Query("provider_token") providerToken: String?): Observable<Token>

    @GET("api/v1/projects")
    fun getProjects(
        @Query("access_token") accessToken: String?,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?
    ): Observable<ProjectResponse>

    @GET("api/v1/me")
    fun getMe(
        @Query("access_token") accessToken: String?
    ): Observable<MeResponse>

    @GET("api/v1/issues")
    fun getIssues(
        @Query("access_token") accessToken: String?,
        @Query("project_id") projectId : Int?,
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?

    ): Observable<IsuesResponse>

    @GET("api/v1/issues/search ")
    fun getSearch(
        @Query("access_token") accessToken: String?,
        @Query("keyword") keyword: String,
        @Query("project_id") projectId: Int?,
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?

    ): Observable<SearchResponse>

    @GET("api/v1/issues/{id} ")
    fun getDetail(
        @Path("id") isues_id: Int?,
        @Query("access_token") accessToken: String?

    ): Observable<DetailResponse>

    @Multipart
    @POST("api/v1/issues")
    fun postIssues (
        @Part("access_token") accessToken: RequestBody,
        @Part("project_id") projectId: RequestBody?,
        @Part("title") title: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("severity_id") severity_id: RequestBody?,
        @Part("link") link: RequestBody?,
        @Part image: MultipartBody.Part?
      //  token: MultipartBody.Part

    ): Observable<ResponseBody>

    @GET("api/v1/me/submission ")
    fun getSub(
        @Query("access_token") accessToken: String?,
        @Query("sort_by") sortBy : String?,
        @Query("q[severity_id_eq]") sortSeverity : Int?,
        @Query("page") page: Int?,
        @Query("per_page") per_page: Int?

    ): Observable<IsuesResponse>

    @DELETE("api/v1/issues/{id}")
    fun delIssues(
        @Path("id") isues_id: String?,
        @Query("access_token") accessToken: String?

    ): Observable<ResponseBody>

    @GET("api/v1/severities ")
    fun getSeverity(
        @Query("access_token") accessToken: String?
    ): Observable<SeverityResponse>



    companion object Factory {
        fun create(): ApiServiceInterface {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.app_referer)
                .build()

            return retrofit.create(ApiServiceInterface::class.java)
        }


        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }


}