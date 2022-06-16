package de.hhn.aib.labsw.blackmirror.controller.API.Websockets

import com.fasterxml.jackson.databind.JsonNode

/**
 * SendPackage that contains the information to be send
 * @author Luis Gutzeit
 * @version 07.05.2022
 */
data class SendPackage(
    val topic: String,
    val payload: JsonNode)
