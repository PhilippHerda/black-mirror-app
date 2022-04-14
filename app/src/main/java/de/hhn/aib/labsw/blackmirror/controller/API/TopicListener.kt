package de.hhn.aib.labsw.blackmirror.controller.API

import com.fasterxml.jackson.databind.JsonNode

interface TopicListener {
    fun dataReceived(topic: String?, `object`: JsonNode?)
}