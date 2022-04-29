package de.hhn.aib.labsw.blackmirror

import android.content.Intent
import java.util.*

/**
 * API to connect and communicate with a Ble server.
 * @author Markus Marewitz
 * @version 2022-04-29
 */
interface BleAPI {

    // METHODS

    /**
     * Ensures that the application has all required permissions.
     * @param onRequestPermissions Callback for when the API wants to request permissions. Ideally pass `Activity.requestPermissions()` function.
     * @param onPermissionsEnsured Callback for when all required permissions have been ensured.
     * @param onPermissionsDenied Callback for when the user denied any of the required permissions.
     * @return A placeholder object that the user of the API should pass the arguments of onRequestPermissionsResult to, like so:
     * ```
     * val pendingReq = bleAPI.ensurePermissions( … )
     * override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
     *    // call super
     *    pendingReq.onResult(requestCode, permissions, grantResults)
     * }
     * ```
     * or `null` if no permissions were requested.
     */
    fun ensurePermissions(
        onRequestPermissions: (Array<String>, Int) -> Unit,
        onPermissionsEnsured: () -> Unit,
        onPermissionsDenied: () -> Unit = {}
    ): PendingPermissionsRequest?

    /**
     * Ensures that the required services (BT, Location) are online.
     * @param onServicesEnsured Callback for when the services availability has been ensured.
     * @param onServiceRequestRejected Callback for when the user rejected the requests to enable the required services.
     * @return A function that the user of the API should call when onActivityResult is called, like so:
     * ```
     * val apiServicesCallback = bleAPI.ensureServices( … )
     * override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     *    // call super
     *    apiServicesCallback(requestCode, resultCode, data)
     * }
     * ```
     */
    fun ensureServices(
        onServicesEnsured: () -> Unit,
        onServiceRequestRejected: () -> Unit = {}
    ): (Int, Int, Intent?) -> Unit

    /**
     * Sets the duration of the BT scan.
     * @param millis How long the device should be scanning before it stops.
     */
    fun setScanDuration(millis: Long)

    /**
     * Scans for BT devices and checks if they offer the requested service.
     * @param serviceUUID The UUID of the requested service. Each device is checked for whether it offers this service.
     * @param onServiceFound Callback for when a device is found that offers the requested service. `gatt` is the Generic Attribute Profile. `service` is the requested service.
     * @param onTimeout Callback for when the scan duration exceeds. This is only called if no device has been found during the scan.
     * @throws SecurityException When the required permissions haven't been granted. Call [ensurePermissions] to ensure the required permissions have been granted.
     * @throws ServiceUnavailableException When the required services (BT, Location) are offline. Call [ensureServices] to ensure the required services are online.
     */
    fun scanForService(
        serviceUUID: UUID,
        onServiceFound: (service: Service) -> Unit,
        onTimeout: () -> Unit = {}
    )

    /**
     * Stops scanning for BT devices.
     * @throws SecurityException  When the required permissions haven't been granted. Call [ensurePermissions] to ensure the required permissions have been granted.
     * @throws ServiceUnavailableException When the required services (BT, Location) are offline. Call [ensureServices] to ensure the required services are online.
     */
    fun stopScanning()

    // INTERFACES

    /**
     * Ble Service interface that allows direct access to characteristics.
     * @author Markus Marewitz
     * @version 2022-04-26
     */
    interface Service {
        /**
         * Sets the value of the specified characteristic and publishes the change.
         * @param value The new value of the characteristic.
         * @param characteristicUUID The UUID of the characteristic.
         * @throws CharacteristicNotFoundException If the service doesn't have a characteristic with the specified UUID.
         * @throws SecurityException When the required permissions haven't been granted. Call [ensurePermissions] to ensure the required permissions have been granted.
         * @throws ServiceUnavailableException When the required services (BT, Location) are offline. Call [ensureServices] to ensure the required services are online.
         */
        fun writeCharacteristic(
            value: String,
            characteristicUUID: UUID
        )
    }

    /**
     * Represents a pending permission request.
     * @author Markus Marewitz
     * @version 2022-04-29
     */
    interface PendingPermissionsRequest {
        /**
         * Call this when a permission request has been answered. Pass the arguments of `onPermissionRequestResult()` to this function.
         */
        fun onResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    }

    // EXCEPTIONS

    /**
     * This exception is thrown when an operation cannot be performed because a required service is unavailable.
     * When catching this exception the unavailable service can be queried with [type].
     * @author Markus Marewitz
     * @version 2022-04-26
     */
    class ServiceUnavailableException : RuntimeException {
        constructor(type: Type) : super() {
            this.type = type
        }

        constructor(type: Type, message: String) : super(message) {
            this.type = type
        }

        constructor(type: Type, message: String, cause: Throwable) : super(message, cause) {
            this.type = type
        }

        constructor(type: Type, cause: Throwable) : super(cause) {
            this.type = type
        }

        enum class Type { BLUETOOTH, LOCATION }

        val type: Type
    }

    /**
     * This exception is thrown when a characteristic is not contained inside a service.
     * Get the missing characteristics UUID with [uuid].
     * @author Markus Marewitz
     * @version 2022-04-26
     */
    class CharacteristicNotFoundException : RuntimeException {
        constructor(uuid: UUID) : super() {
            this.uuid = uuid
        }

        constructor(uuid: UUID, message: String) : super(message) {
            this.uuid = uuid
        }

        constructor(uuid: UUID, message: String, cause: Throwable) : super(message, cause) {
            this.uuid = uuid
        }

        constructor(uuid: UUID, cause: Throwable) : super(cause) {
            this.uuid = uuid
        }

        val uuid: UUID
    }
}
