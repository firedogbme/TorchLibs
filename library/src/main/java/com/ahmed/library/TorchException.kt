package com.ahmed.library

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.lang.Exception

class TorchException(message: String?,context: Context,isShowToast: Boolean) : Exception(message) {

    companion object{
        private const val TAG = "TorchException"
    }

    init {
        Log.e(TAG, message!!)
        if (isShowToast){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}