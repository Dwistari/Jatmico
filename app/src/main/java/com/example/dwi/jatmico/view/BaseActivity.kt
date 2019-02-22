package com.example.dwi.jatmico.view

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreSavedData(savedInstanceState)
        setContentView(getLayout())
        onViewReady()
        progressDialog = ProgressDialog(this@BaseActivity)
    }

    protected open fun restoreSavedData(savedInstanceState: Bundle?) {}

    abstract fun getLayout(): Int

    abstract fun onViewReady()

    init {
        progressDialog?.setCancelable(false)
        progressDialog?.setCanceledOnTouchOutside(false)
        progressDialog?.setMessage("Loading")
    }

    fun showProgressDialog() {
        progressDialog?.show()
    }
    fun hideProgressDialog() {
        progressDialog?.dismiss()
    }
}