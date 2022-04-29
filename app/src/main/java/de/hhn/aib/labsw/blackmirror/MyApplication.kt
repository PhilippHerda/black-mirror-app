package de.hhn.aib.labsw.blackmirror

import android.app.Application

class MyApplication : Application() {
    val bleAPI: BleAPI = BleAPIImpl(this)
}
