package de.hhn.aib.labsw.blackmirror.controller.API.Websockets

import com.fasterxml.jackson.databind.JsonNode

/**
 * SendPackage that contains the information to be send
 */
data class SendPackage(
    val topic: String,
    val payload: JsonNode)
