package com.example.dwi.jatmico.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchResponse (
    @SerializedName("response")
    @Expose
    var search: MutableList<Search>
)

data class ImageSearch(
    @SerializedName("url")
    @Expose
    var url: String,
    @SerializedName("thumb")
    @Expose
    var thumb: ThumbSearch,
    @SerializedName("medium")
    @Expose
    var medium: MediumSearch,
    @SerializedName("large")
    @Expose
    var large: LargeSearch ?
)

data class LargeSearch(
    @SerializedName("url")
    @Expose
    var url: String?
)

data class MediumSearch(
    @SerializedName("url")
    @Expose
    var url: String?
)

data  class Search(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("project_id")
    @Expose
    var projectId: String,
    @SerializedName("user")
    @Expose
    var user: UserSearch,
    @SerializedName("title")
    @Expose
    var title: String,
    @SerializedName("description")
    @Expose
    var description: String,
    @SerializedName("severity")
    @Expose
    var severity: SeveritySearch,
    @SerializedName("image")
    @Expose
    var image: ImageSearch,
    @SerializedName("created_at")
    @Expose
    var createdAt : String,
    @SerializedName("updated_at")
    @Expose
    var updated_at : String
)
data class SeveritySearch(
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
)

data class ThumbSearch(
    @SerializedName("url")
    @Expose
    var url: String
)

data class UserSearch(
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
