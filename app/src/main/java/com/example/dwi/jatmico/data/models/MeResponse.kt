package com.example.dwi.jatmico.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MeResponse(
    @SerializedName("response")
    @Expose
    var me : Me?
)

data class Images(
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
data class Larges(
    @SerializedName("url")
    @Expose
    var url: String?
)

data class Mediums(
     @SerializedName("url")
     @Expose
     var url: String?
 )
  data  class Me(
      @SerializedName("id")
      @Expose
      var id: Int,
      @SerializedName("name")
      @Expose
      var name: String,
      @SerializedName("location")
      @Expose
      var location: String,
      @SerializedName("image")
      @Expose
      var image: Image
)
data class Thumbs(
    @SerializedName("url")
    @Expose
    var url: String
)