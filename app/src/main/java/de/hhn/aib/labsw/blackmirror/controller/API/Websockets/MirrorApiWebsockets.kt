package de.hhn.aib.labsw.blackmirror.controller.API.Websockets

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.*
import java.io.IOException

/**
 * Implementation of the interface using websockets
 * @author Luis Gutzeit
 * @version 05.05.2022
 */
class MirrorApiWebsockets : WebSocketListener(),MirrorApi {
    private val sessions = mutableListOf<WebSocket>()
    private val listeners = mutableMapOf<String,MutableList<ApiListener>>()

    val mapper: ObjectMapper = ObjectMapper()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        sessions.add(webSocket)
        print("new Connection!: ")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        try {
            val jsonNode: JsonNode = mapper.readTree(text)
            val topic: String = jsonNode.get("topic").textValue()
            requireNotNull(jsonNode.get("payload")) { "wrong json format" }
            val listenersList = listeners[topic]
            if(listenersList != null) {
                val listIterator = listenersList.iterator()
                while (listIterator.hasNext()) {
                    listIterator.next().dataReceived(topic, jsonNode)
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        // WebSocket connection closes
        print("Connection closed: ")
        sessions.remove(webSocket)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        webSocket.close(1013, "try again later")
        sessions.remove(webSocket)
    }

    /**
     * add a subscription on a specific topic
     * @param topic topic to subscribe, not null
     * @param listener listener to receive updates, not null
     */
    override fun subscribe(topic: String, listener: ApiListener) {
        var listenerList = listeners[topic]
        if (listenerList == null) {
            listenerList = mutableListOf(listener)
            listeners[topic] = listenerList
        } else {
            listenerList.add(listener)
        }
    }

    /**
     * revoke a subscription on a specific topic
     * @param topic topic of the subscription, not null
     * @param listener listener to remove, not null
     */
    override fun unsubscribe(topic: String, listener: ApiListener) {
        val listenerList  = listeners[topic]
        listenerList?.remove(listener)
    }

    /**
     * send a message to the mirror
     * @param topic topic of the message, not null
     * @param payload payload of the message, not null
     */
    override fun publish(topic: String, payload: Any) {
        val sendPackage = SendPackage(topic, mapper.valueToTree(payload))
        sessions.forEach { session: WebSocket ->
            try {
                session.send(mapper.writeValueAsString(sendPackage))
            } catch (e: IOException) {
                println(e.message)
            }
        }
    }

    /**
     * send a message to the mirror
     * @param topic topic of the message, not null
     * @param payload payload of the message, not null
     */
    override fun publish(topic: String, payload: JsonNode) {
        val sendPackage = SendPackage(topic,payload)
        sessions.forEach { session: WebSocket ->
            try {
                session.send(mapper.writeValueAsString(sendPackage))
            } catch (e: IOException) {
                println(e.message)
            }
        }
    }
}