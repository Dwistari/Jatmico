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
