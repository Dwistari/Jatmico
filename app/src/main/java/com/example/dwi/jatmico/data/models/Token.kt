package com.example.dwi.jatmico.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("access_token")
    @Expose
    var access_token: String,
    @SerializedName("token_type")
    @Expose
    var token_type: String,
    @SerializedName("expires_id")
    @Expose
    var expires_in: String,
    @SerializedName("refresh_token")
    @Expose
    var refresh_token: String,
    @SerializedName("created_at_token")
    @Expose
    var created_at_token: String
)