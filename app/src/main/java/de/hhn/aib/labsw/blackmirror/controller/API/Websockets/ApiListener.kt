package de.hhn.aib.labsw.blackmirror.controller.API.Websockets

import com.fasterxml.jackson.databind.JsonNode

interface ApiListener {
    fun dataReceived(topic: String, `object`: JsonNode)
}