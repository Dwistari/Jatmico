package com.example.dwi.report.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SimpleGetModel(
    @SerializedName("response")
    @Expose
    var response: String? = null
): BaseModel