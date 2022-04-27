package de.hhn.aib.labsw.blackmirror

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattService
import android.content.Context
import java.io.Closeable
import java.util.*

/**
 * @author Markus Marewitz
 * @version 2022-04-27
 */
class BleAPIImpl(private val context: Context) : BleAPI {

    // FIELDS

    // scans for 20 sec by default
    private var scanDurationMillis: Long = 20 * 1000;

    // INTERFACE METHODS

    override fun ensurePermissions(
        onPermissionsEnsured: () -> Unit,
        onPermissionsDenied: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun ensureServices(
        onServicesEnsured: () -> Unit,
        onServiceRequestRejected: () -> Unit
    ) {
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

    // IMPLEMENTATION METHODS

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
}
