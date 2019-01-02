package com.example.dwi.report

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.dwi.report.api.ApiProvider
import com.example.dwi.report.api.ApiResult
import com.example.dwi.report.model.BaseModel
import com.example.dwi.report.model.SimpleGetModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedInputStream


class MainActivity : AppCompatActivity() {

    private val TAG = "ApiProvider"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCall.setOnClickListener {
            resultText.text = "Making call..."
            ApiProvider().callApi(object : ApiResult {
                override fun onError(e: Exception) {
                    Log.e(TAG, e.message)
                }

                override fun onModel(baseModel: BaseModel) {
                    if (baseModel is SimpleGetModel) {
                        val date = baseModel.response
                        resultText.text = "date: " + date
                    }
                }


                override fun onJson(jsonObject: JSONObject) {
                    Log.e(TAG, "Received a different model")
                }

                override fun onAPIFail() {
                    Log.e(TAG, "Failed horribly")
                }

            })
        }
    }
}

