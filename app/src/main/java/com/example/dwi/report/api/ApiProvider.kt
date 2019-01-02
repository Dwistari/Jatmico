package com.example.dwi.report.api

import com.example.dwi.report.Constants
import android.provider.SyncStateContract
import android.util.Log
import com.example.dwi.report.model.SimpleGetModel
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ApiProvider {

    private val TAG = "ApiProvider"

    private val mApiServiceNetwork = ApiServiceNetwork.getInstance()

    fun callApi (apiResult: ApiResult){
        try {
            mApiServiceNetwork.getNetworkService(Constants.API_ENDPOINT)
                .getSampleData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<SimpleGetModel>() {
                    override fun onCompleted() {
                        //Do nothing for now
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "onError" + Log.getStackTraceString(e))
                        apiResult.onAPIFail()
                    }
                    override fun onNext(sampleGetModel: SimpleGetModel) {
                        Log.i(TAG, "Operation performed successfully")
                        apiResult.onModel(sampleGetModel)
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, "Exception" + Log.getStackTraceString(e))
            apiResult.onError(e)
        }

    }


}