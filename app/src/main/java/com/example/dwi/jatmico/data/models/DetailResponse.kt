package com.example.dwi.jatmico.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailResponse (

    @SerializedName("response")
    @Expose
    var detail: Detail?
)
data class ImageDetail(
    @SerializedName("url")
    @Expose
    var url: String,
    @SerializedName("thumb")
    @Expose
    var thumb: ThumbDetail,
    @SerializedName("medium")
    @Expose
    var medium: MediumDetail,
    @SerializedName("large")
    @Expose
    var large: LargeDetail ?
): Serializable

data class LargeDetail(
    @SerializedName("url")
    @Expose
    var url: String?
): Serializable


data class MediumDetail(
    @SerializedName("url")
    @Expose
    var url: String?
): Serializable

data  class Detail(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("project_id")
    @Expose
    var projectId: String,
    @SerializedName("user")
    @Expose
    var user: UserDetail,
    @SerializedName("title")
    @Expose
    var title: String,
    @SerializedName("description")
    @Expose
    var description: String,
    @SerializedName("severity")
    @Expose
    var severity: SeverityDetail,
    @SerializedName("link")
    @Expose
    var link: String,
    @SerializedName("image")
    @Expose
    var image: ImageDetail,
    @SerializedName ("created_at")
    @Expose
    var createdAt : String,
    @SerializedName ("updated_at")
    @Expose
    var updated_at : String
): Serializable

data class SeverityDetail(
    @SerializedName("id")
    @Expose
    var id: String,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("level")
    @Expose
    var level: String,
    @SerializedName("color")
    @Expose
    var color: String
): Serializable

data class ThumbDetail(
    @SerializedName("url")
    @Expose
    var url: String
): Serializable

data class UserDetail(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("image")
    @Expose
    var image: Image
): Serializable
