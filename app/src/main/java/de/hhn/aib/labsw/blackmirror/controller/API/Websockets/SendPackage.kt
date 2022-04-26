package de.hhn.aib.labsw.blackmirror.controller.API.Websockets

import com.fasterxml.jackson.databind.JsonNode

class SendPackage {
    var topic: String? = null
    var payload: JsonNode? = null
}