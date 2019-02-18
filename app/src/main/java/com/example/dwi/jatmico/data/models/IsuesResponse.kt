package com.example.dwi.jatmico.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class IsuesResponse (
    @SerializedName("response")
    @Expose
    var isues: MutableList<Isues>
    )

    data class ImageIsues(
        @SerializedName("url")
        @Expose
        var url: String,
        @SerializedName("thumb")
        @Expose
        var thumb: ThumbIsues,
        @SerializedName("medium")
        @Expose
        var medium: MediumIsues,
        @SerializedName("large")
        @Expose
        var large: LargeIsues ?
        )

    data class LargeIsues(
        @SerializedName("url")
        @Expose
        var url: String?
    )

    data class MediumIsues(
        @SerializedName("url")
        @Expose
        var url: String?
    )

    data  class Isues(
        @SerializedName("id")
        @Expose
        var id: Int,
        @SerializedName("project_id")
        @Expose
        var projectId: Int,
        @SerializedName("user")
        @Expose
        var user: User,
        @SerializedName("title")
        @Expose
        var title: String,
        @SerializedName("description")
        @Expose
        var description: String,
        @SerializedName("severity")
        @Expose
        var severity: Severity,
        @SerializedName("link")
        @Expose
        var link: String,
        @SerializedName("image")
        @Expose
        var image: ImageIsues,
        @SerializedName ("created_at")
        @Expose
        var createdAt : String,
        @SerializedName ("updated_at")
        @Expose
        var updated_at : String
    ): Serializable
        
    data class ThumbIsues(
        @SerializedName("url")
        @Expose
        var url: String
    )

    data class User(
        @SerializedName("id")
        @Expose
        var id: String,
        @SerializedName("name")
        @Expose
        var name: String,
        @SerializedName("image")
        @Expose
        var image: Image
    )
