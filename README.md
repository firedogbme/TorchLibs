# Torch

Torch is a simple-use library for Android hepls you to have an access on the flash light.

## Gradle

**Step 1.** Add the JitPack repository to your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
      maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the library dependency to your project build.gradle:
```
dependencies {
  implementation 'com.github.AhmedAbdEllatiif:Torch:(lastest release version)'
}
```

## Usage

Sample code (Java):
```
  TorchProvider.Builder builder = new TorchProvider.Builder(this);
        builder.repeat(true)
            .repeatTimes(10)
            .intervalTime(100)
            .waitFor(5000)
            .infinite(true)
            .showToastException(true)
            .setTorchOnCallBack(new TorchProvider.Builder.OnTorchChanged() {
                    @Override
                    public void onTorchModeUnavailable(@NotNull String s) {
                         Log.d(TAG, "onTorchModeUnavailable: ")
                    }

                    @Override
                    public void onTorchModeChanged(@NotNull String s, boolean b) {
                      Log.d(TAG, "onTorchModeChanged: ")
                    }
                })
            .start()
```

Sample code (Kotlin):
```
   val builder = TorchProvider.Builder(this)
        builder
            .repeat(true)
            .repeatTimes(10)
            .intervalTime(100)
            .waitFor(5000)
            .infinite(true)
            .showToastException(true)
            .setTorchOnCallBack(object : TorchProvider.Builder.OnTorchChanged {
                override fun onTorchModeUnavailable(cameraId: String) {
                    Log.d(TAG, "onTorchModeUnavailable: ")
                }

                override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                    Log.d(TAG, "onTorchModeChanged: ")
                }
            })
            .start()
```

| Usage                                | Description                                                                           | 
| ------------------------------------ |-------------------------------------------------------------------------------------- | 
| `builder.repeat(boolean)`     |  To repeat the flash (default false)| 
| `builder.repeatTimes(int)`   |  Num of repeat times (default repeats 2 times)                               | 
| `builder.intervalTime(Long)`         | defines the interval time between flash ON and OFF (default 100 ms)                                                       | 
| `builder.waitFor(Long)`  |  wait for some time then the flash goes OFF                                              | 
| `builder.infinite(boolean)`  |  open flash forever (default false)                                 | 
| `builder.showToastException(boolean)`  |  show the exception instance of (TorchException) in a toast message (default false)                                 | 
| `builder.setTorchOnCallBack(TorchProvider.Builder.OnTorchChanged())`  |  call back of the flash light                               | 
| `builder.start()`  |  start the flash light with pervious configuration                               |
