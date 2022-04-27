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
        onTimeout: () -> Unit
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

    // EXCEPTIONS

    /**
     * This exception is thrown when an operation cannot be performed because a required service is unavailable.
     * When catching this exception the unavailable service can be queried with [type].
     * @author Markus Marewitz
     * @version 2022-04-26
     */
    class ServiceUnavailableException : RuntimeException {
        constructor(type: Type) : super() { this.type = type }
        constructor(type: Type, message: String) : super(message) { this.type = type }
        constructor(type: Type, message: String, cause: Throwable) : super(message, cause) { this.type = type }
        constructor(type: Type, cause: Throwable) : super(cause) { this.type = type }

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
        constructor(uuid: UUID) : super() { this.uuid = uuid }
        constructor(uuid: UUID, message: String) : super(message) { this.uuid = uuid }
        constructor(uuid: UUID, message: String, cause: Throwable) : super(message, cause) { this.uuid = uuid }
        constructor(uuid: UUID, cause: Throwable) : super(cause) { this.uuid = uuid }

        val uuid: UUID
    }
}
