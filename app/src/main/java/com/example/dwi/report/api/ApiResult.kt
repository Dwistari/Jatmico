package com.example.dwi.report.api

import com.example.dwi.report.model.BaseModel
import org.json.JSONObject
import java.lang.Exception

interface ApiResult {

    fun onError(exception: Exception)
    fun onModel(baseModel: BaseModel)
    fun onJson(jsonObject: JSONObject)
    fun onAPIFail()
}