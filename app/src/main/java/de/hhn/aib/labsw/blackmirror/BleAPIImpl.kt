package de.hhn.aib.labsw.blackmirror

import android.Manifest
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattService
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import java.io.Closeable
import java.util.*
import kotlin.concurrent.thread

/**
 * @author Markus Marewitz
 * @version 2022-04-29
 */
class BleAPIImpl(private val context: Context) : BleAPI {

    /////////////////////////////////////////////////////////////////////////////////////////
    //  FIELDS                                                                             //
    /////////////////////////////////////////////////////////////////////////////////////////

    // scans for 20 sec by default
    private var scanDurationMillis: Long = 20 * 1000


    /////////////////////////////////////////////////////////////////////////////////////////
    //  INTERFACE METHODS                                                                  //
    /////////////////////////////////////////////////////////////////////////////////////////

    override fun ensurePermissions(
        onRequestPermissions: (Array<String>, Int) -> Unit,
        onPermissionsEnsured: () -> Unit,
        onPermissionsDenied: () -> Unit
    ): BleAPI.PendingPermissionsRequest? {
        val missingPermissions = getMissingPermissions()
        if (missingPermissions.isNotEmpty()) {
            onRequestPermissions(missingPermissions.toTypedArray(), PERMISSIONS_REQUEST_CODE)
            return PendingPermissionsRequestImpl(onPermissionsEnsured, onPermissionsDenied)
        }
        thread { onPermissionsEnsured() }
        return null
    }

    override fun ensureServices(
        onServicesEnsured: () -> Unit,
        onServiceRequestRejected: () -> Unit
    ): (Int, Int, Intent?) -> Unit {
        TODO("Not yet implemented")
    }

    override fun setScanDuration(millis: Long) {
        this.scanDurationMillis = millis
    }

    override fun scanForService(
        serviceUUID: UUID,
        onServiceFound: (service: BleAPI.Service) -> Unit,
        onTimeout: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun stopScanning() {
        TODO("Not yet implemented")
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    //  IMPLEMENTATION METHODS                                                             //
    /////////////////////////////////////////////////////////////////////////////////////////

    // PERMISSIONS //////////////////////////////////////////////////////////////////////////

    private fun getMissingPermissions(): List<String> {
        return getRequiredPermissions().toMutableList().filter {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_DENIED
        }
    }

    // SERVICES /////////////////////////////////////////////////////////////////////////////

    private fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        TODO("Not yet implemented")
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    //  COMPANION OBJECT                                                                   //
    /////////////////////////////////////////////////////////////////////////////////////////

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 1658486468

        /**
         * @return A list of required permissions. This depends on the Android SDK.
         */
        fun getRequiredPermissions(): List<String> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                listOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,

                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            else
                listOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    //  CLASSES                                                                            //
    /////////////////////////////////////////////////////////////////////////////////////////

    // PERMISSIONS //////////////////////////////////////////////////////////////////////////

    /**
     * Implementation of the [BleAPI.PendingPermissionsRequest] interface. Objects of this class
     * hold the callbacks for when the result arrives.
     * This class is a basically a promise.
     * @author Markus Marewitz
     * @version 2022-04-29
     */
    private class PendingPermissionsRequestImpl(
        private val onPermissionsGranted: () -> Unit,
        private val onPermissionsDenied: () -> Unit
    ) : BleAPI.PendingPermissionsRequest {
        override fun onResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            if (requestCode != PERMISSIONS_REQUEST_CODE) return
            for (grantedStatus in grantResults) {
                if (grantedStatus != PackageManager.PERMISSION_GRANTED) {
                    thread { onPermissionsDenied() }
                }
            }
            thread { onPermissionsGranted() }
        }
    }

    // PERMISSIONS //////////////////////////////////////////////////////////////////////////

    /**
     * Implementation for the [BleAPI.Service] interface. It implements [Closeable] so that the
     * service is automatically disconnected when this objects gets destroyed.
     * @author Markus Marewitz
     * @version 2022-04-27
     */
    class ServiceImpl(
        private val gatt: BluetoothGatt,
        private val gattService: BluetoothGattService
    ) : BleAPI.Service, Closeable {
        override fun writeCharacteristic(value: String, characteristicUUID: UUID) {
            TODO("Not yet implemented")
        }

        override fun close() {
            TODO("Not yet implemented")
        }
    }
}
