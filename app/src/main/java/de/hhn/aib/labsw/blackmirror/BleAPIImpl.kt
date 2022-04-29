package de.hhn.aib.labsw.blackmirror

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import java.io.Closeable
import java.util.*

/**
 * @author Markus Marewitz
 * @version 2022-04-29
 */
class BleAPIImpl(private val context: Context) : BleAPI {

    // FIELDS

    // scans for 20 sec by default
    private var scanDurationMillis: Long = 20 * 1000

    // INTERFACE METHODS

    override fun ensurePermissions(
        onPermissionsEnsured: () -> Unit,
        onPermissionsDenied: () -> Unit
    ): (Int, Array<out String>, IntArray) -> Unit {
        context.startActivity(Intent(context, EnsurePermissionsActivity::class.java))
        TODO("Not yet implemented")
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

    // CLASSES

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

    // ACTIVITIES

    private class EnsurePermissionsActivity : AppCompatActivity() {
    }
}
