package de.hhn.aib.labsw.blackmirror.controller.API.Bluetooth

import android.bluetooth.BluetoothDevice
import java.util.*

interface BluetoothLeApi {
    fun findDeviceWithService(serviceUuid: UUID,callback:(device:BluetoothDevice)->Unit)
    fun writeCharacteristic(text:String, uuid:UUID)

}