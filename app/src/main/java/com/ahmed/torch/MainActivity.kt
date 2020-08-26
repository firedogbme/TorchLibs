package com.ahmed.torch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.ahmed.library.TorchProvider



class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      // val btnStart : Button = findViewById(R.id.btnStart)

    }


    fun openTorch(view : View) {
        val builder = TorchProvider.Builder(this)
        builder
            .infinite(true)
            .setTorchOnCallBack(object : TorchProvider.Builder.OnTorchChanged {
                override fun onTorchModeUnavailable(cameraId: String) {
                    Log.e(TAG, "onTorchModeUnavailable: ")
                }

                override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                    Log.e(TAG, "onTorchModeChanged: ")
                }
            })
            .start()
    }

    fun waitFor(view: View){
        val builder = TorchProvider.Builder(this)
        builder
            .waitFor(5000)
            .showToastException(true)
            .start()
    }


    fun repeatFlash(view: View){
        val builder = TorchProvider.Builder(this)
        builder
            .repeat(true)
            .repeatTimes(10)
            .intervalTime(100)
            .showToastException(true)
            .start()
    }






}