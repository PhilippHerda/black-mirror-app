package de.hhn.aib.labsw.blackmirror

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    var pendingReq: BleAPI.PendingPermissionsRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serviceUUID = UUID.randomUUID()
        val characteristicUUID = UUID.randomUUID()

        val bleAPI = (application as MyApplication).bleAPI
        pendingReq = bleAPI.ensurePermissions(
            this::requestPermissions,
            onPermissionsEnsured = {
                bleAPI.ensureServices(
                    onServicesEnsured = {
                        bleAPI.scanForService(
                            serviceUUID,
                            onServiceFound = {
                                it.writeCharacteristic("Hello world", characteristicUUID)
                            }
                        )
                    }
                )
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        pendingReq?.onResult(requestCode, permissions, grantResults)
    }
}