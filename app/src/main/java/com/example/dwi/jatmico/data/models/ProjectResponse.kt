package com.example.dwi.jatmico.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class  ProjectResponse(
    @SerializedName("response")
    @Expose
    var projects: MutableList<Project>
)

data class Image(
    @SerializedName("url")
    @Expose
    var url: String,
    @SerializedName("thumb")
    @Expose
    var thumb: Thumb,
    @SerializedName("medium")
    @Expose
    var medium: Medium,
    @SerializedName("large")
    @Expose
    var large: Large?
)

data class Large(
    @SerializedName("url")
    @Expose
    var url: String?
)

data class Medium(
    @SerializedName("url")
    @Expose
    var url: String?
)

data class Project(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("image")
    @Expose
    var image: Image,
    @SerializedName("total_issue")
    @Expose
    var total_issue: Int,
    @SerializedName("tag_list")
    @Expose
    var tag_list: List<String>
)

data class Thumb(
    @SerializedName("url")
    @Expose
    var url: String
)