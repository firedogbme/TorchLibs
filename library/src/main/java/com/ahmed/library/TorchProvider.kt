package com.ahmed.library

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class TorchProvider{

    companion object {
        private const val TAG = "TorchProvider"
        private const val DEFAULT_REPEAT_TIMES: Int = 2
        private const val DEFAULT_INTERVAL_TIME: Long = 100
        private const val DEFAULT_WAIT_FOR: Long = -150
    }


    class Builder constructor(private val context: Context) {
        private var intervalTime: Long = DEFAULT_INTERVAL_TIME
        private var repeatTimes: Int = DEFAULT_REPEAT_TIMES
        private var waitFor: Long = DEFAULT_WAIT_FOR
        private var infinite: Boolean = false
        private var repeat: Boolean = false
        private var cameraManager: CameraManager? = null
        private var cameraId: String? = null
        private lateinit var torchCallback: CameraManager.TorchCallback
        private var b: Boolean = false
        private var showToastException: Boolean = false


        init {
            b = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                cameraId = cameraManager!!.cameraIdList[0]
                torchCallback = object : CameraManager.TorchCallback() {}

            }

        }


//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                          Builder methods                                                      *\\
//*                                                                                                                 *\\
//***********************************************************************************************************************/
        /**
         *To show toast with the exception default false
         * */
        fun showToastException(isShow: Boolean): Builder {
            this.showToastException = isShow
            return this
        }
        /**
         * This method defines the num of repeat time
         * */

        fun repeat(isRepeat: Boolean): Builder {
            this.repeat = isRepeat
            return this
        }
        /**
         * This method defines the lag between every ON and OFF
         * */
        fun intervalTime(intervalTimeInMilliseconds: Long): Builder {
            this.intervalTime = intervalTimeInMilliseconds
            return this
        }

        /**
         * This method defines the num of repeat time
         * */

        fun repeatTimes(repeatTimes: Int): Builder {
            this.repeatTimes = repeatTimes
            return this
        }

        /**
         * This method wait for timeInMilliseconds then turn the torch off
         * */
        fun waitFor(waitForInMilliseconds: Long): Builder {
            this.waitFor = waitForInMilliseconds
            return this
        }

        /**
         * This method turn the torch on for ever
         * */
        fun infinite(isInfinite: Boolean): Builder {
            this.infinite = isInfinite
            return this
        }

        /**
         * This method set call a listener on torch call back status
         * */
        fun setTorchOnCallBack(onTorchChanged: OnTorchChanged): Builder {
            torchCallback = @RequiresApi(Build.VERSION_CODES.M)
            object : CameraManager.TorchCallback() {
                override fun onTorchModeUnavailable(cameraId: String) {
                    super.onTorchModeUnavailable(cameraId)
                    onTorchChanged.onTorchModeUnavailable(cameraId)
                }

                override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                    super.onTorchModeChanged(cameraId, enabled)
                    onTorchChanged.onTorchModeChanged(cameraId, enabled)
                }
            }
            return this
        }

        /**
         * To start using torch after build it
         * */
        fun start() {
            try {

                showException("There is no flash light with camera \"$cameraId\"")

                if (infinite) {
                    startTorchForEver()
                    return
                }

                if (repeat){
                    if (intervalTime != DEFAULT_INTERVAL_TIME) {
                        startTorchWithInterval()
                        return
                    }
                }

                if (waitFor != DEFAULT_WAIT_FOR) {
                    startTorchWithWaitFor()
                    return
                }




            } catch (e: TorchException) {
            }


        }





//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                          Torch ON/OFF                                                         *\\
//*                                                                                                                 *\\
//***********************************************************************************************************************/
        /**
         * To turn ON the torch
         * */
        private fun openTorch() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager!!.setTorchMode(cameraId!!, true)
            } else {
                Log.e(TAG, "openTorch: ")
            }
        }

        /**
         * To turn OFF the torch
         * */
        private fun closeTorch() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager!!.setTorchMode(cameraId!!, false)
            }
        }


//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                          Threads                                                              *\\
//*                                                                                                                 *\\
//***********************************************************************************************************************/

        /**To start a thread with to open torch for ever*/
        private fun startTorchForEver() {
            Thread(Runnable {
                openTorch()
            }).start()
            Log.d(TAG, "startTorchForEver")
        }

        /**To start a thread with waitFor*/
        private fun startTorchWithWaitFor() {
            Thread(Runnable {
                openTorch()
                Thread.sleep(waitFor)
                closeTorch()
            }).start()

        }

        /**To start a thread with intervalTime*/
        private fun startTorchWithInterval() {
            Thread(Runnable {
                for (i in 0 until repeatTimes step 1) {
                    openTorch()
                    Thread.sleep(intervalTime)
                    closeTorch()
                }
            }).start()

        }


//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                          Exceptions                                                          *\\
//*                                                                                                                 *\\
//***********************************************************************************************************************/

        /**
         * @throws TorchException
         * To Show exception
         * */
        private fun showException(s: String) {
            if (!b) {
                throw TorchException(s,context,showToastException)
            }
        }



//*************************************************************************************************************\\
//*                                                                                                             *\\
//*                                          interfaces                                                           *\\
//*                                                                                                                 *\\
//***********************************************************************************************************************/
        /**
         * interface with torch changed status
         * */
        interface OnTorchChanged {
            fun onTorchModeUnavailable(cameraId: String)
            fun onTorchModeChanged(cameraId: String, enabled: Boolean)
        }




    }
}