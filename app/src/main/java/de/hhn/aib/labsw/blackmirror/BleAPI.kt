package de.hhn.aib.labsw.blackmirror

import java.util.*

/**
 * API to connect and communicate with a Ble server.
 * @author Markus Marewitz
 * @version 2022-04-26
 */
interface BleAPI {

    // METHODS

    /**
     * Ensures that the application has all required permissions.
     * @param onPermissionsEnsured Callback for when all required permissions have been ensured.
     * @param onPermissionsDenied Callback for when the user denied any of the required permissions.
     */
    fun ensurePermissions(onPermissionsEnsured: () -> Unit, onPermissionsDenied: () -> Unit)

    /**
     * Ensures that the required services (BT, Location) are online.
     * @param onServicesEnsured Callback for when the services availability has been ensured.
     * @param onServiceRequestRejected Callback for when the user rejected the requests to enable the required services.
     */
    fun ensureServices(onServicesEnsured: () -> Unit, onServiceRequestRejected: () -> Unit)

    /**
     * Sets the duration of the BT scan.
     * @param millis How long the device should be scanning before it stops.
     */
    fun setScanDuration(millis: Long)

    /**
     * Scans for BT Devices and checks if they offer the requested service.
     * @param serviceUUID The UUID of the requested service. Each device is checked for whether it offers this service.
     * @param onServiceFound Callback for when a device is found that offers the requested service. `gatt` is the Generic Attribute Profile. `service` is the requested service.
     * @param onTimeout Callback for when the scan duration exceeds. This is only called if no device has been found during the scan.
     * @throws SecurityException When the required permissions haven't been granted. Call [ensurePermissions] to ensure the required permissions have been granted.
     * @throws ServiceUnavailableException When the required services (BT, Location) are offline. Call [ensureServices] to ensure the required services are online.
     */
    fun scanForService(
        serviceUUID: UUID,
        onServiceFound: (service: Service) -> Unit,
        onTimeout: () -> Unit
    )

    // INTERFACES

    interface Service {
        /**
         * Sets the value of the specified characteristic and publishes the change.
         * @param value The new value of the characteristic.
         * @param characteristicUUID The UUID of the characteristic.
         */
        fun writeCharacteristic(
            value: String,
            characteristicUUID: UUID
        )
    }

    // EXCEPTIONS

    class ServiceUnavailableException : RuntimeException {
        constructor() : super()
        constructor(message: String) : super(message)
        constructor(message: String, cause: Throwable) : super(message, cause)
        constructor(cause: Throwable) : super(cause)
    }
}
