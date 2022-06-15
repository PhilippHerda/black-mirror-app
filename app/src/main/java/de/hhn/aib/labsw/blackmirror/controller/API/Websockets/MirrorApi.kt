package de.hhn.aib.labsw.blackmirror.controller.API.Websockets

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Definition of the API methods
 * Author: Luis Gutzeit
 * Version: 1.1 - 19.04.2022
 */
interface MirrorApi {
    fun subscribe(topic: String, listener: ApiListener)
    fun unsubscribe(topic: String, listener: ApiListener)
    fun publish(topic: String, payload: Any): Boolean
    fun publish(topic: String, payload: JsonNode): Boolean
}