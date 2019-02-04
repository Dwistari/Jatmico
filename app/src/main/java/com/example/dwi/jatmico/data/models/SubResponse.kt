//package com.example.dwi.jatmico.data.models
//
//import com.google.gson.annotations.Expose
//import com.google.gson.annotations.SerializedName
//
//class SubResponse (
//@SerializedName("response")
//@Expose
//var submission: MutableList<Submission>
//)
//
//data class ImageSub(
//    @SerializedName("url")
//    @Expose
//    var url: String,
//    @SerializedName("thumb")
//    @Expose
//    var thumb: ThumbIsues,
//    @SerializedName("medium")
//    @Expose
//    var medium: MediumIsues,
//    @SerializedName("large")
//    @Expose
//    var large: LargeIsues ?
//)
//
//data class LargeSub(
//    @SerializedName("url")
//    @Expose
//    var url: String?
//)
//
//data class MediumSub(
//    @SerializedName("url")
//    @Expose
//    var url: String?
//)
//
//data  class Submission(
//    @SerializedName("id")
//    @Expose
//    var id: Int,
//    @SerializedName("project_id")
//    @Expose
//    var projectId: String,
//    @SerializedName("user")
//    @Expose
//    var user: User,
//    @SerializedName("title")
//    @Expose
//    var title: String,
//    @SerializedName("description")
//    @Expose
//    var description: String,
//    @SerializedName("severity")
//    @Expose
//    var severity: Severity,
//    @SerializedName("image")
//    @Expose
//    var image: ImageIsues,
//    @SerializedName("created_at")
//    @Expose
//    var createdAt : String,
//    @SerializedName("updated_at")
//    @Expose
//    var updated_at : String
//)
//data class SeveritySub(
//    @SerializedName("id")
//    @Expose
//    var id: String,
//    @SerializedName("name")
//    @Expose
//    var name: String,
//    @SerializedName("level")
//    @Expose
//    var level: String,
//    @SerializedName("color")
//    @Expose
//    var color: String
//)
//
//data class ThumbSub(
//    @SerializedName("url")
//    @Expose
//    var url: String
//)
//
//data class UserSub(
//    @SerializedName("id")
//    @Expose
//    var id: String,
//    @SerializedName("name")
//    @Expose
//    var name: String,
//    @SerializedName("image")
//    @Expose
//    var image: Image
//)