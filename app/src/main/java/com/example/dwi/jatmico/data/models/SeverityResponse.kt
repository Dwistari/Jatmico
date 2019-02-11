package com.example.dwi.jatmico.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SeverityResponse (
    @SerializedName("response")
    @Expose
    var severity: MutableList<Severitys>
)
data class Severitys(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("level")
    @Expose
    var level: Int,
    @SerializedName("color")
    @Expose
    var color: String
)
