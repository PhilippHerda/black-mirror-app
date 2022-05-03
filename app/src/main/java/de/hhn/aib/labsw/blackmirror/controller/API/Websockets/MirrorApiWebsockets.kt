package de.hhn.aib.labsw.blackmirror.controller.API.Websockets

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.*
import java.io.IOException

class MirrorApiWebsockets : WebSocketListener(),MirrorApi {
    var sessions: ArrayList<WebSocket> = ArrayList()
    private var mapper: ObjectMapper = ObjectMapper()
    var listeners: HashMap<String, ArrayList<ApiListener>?> =
        HashMap()

    fun getJSONMapper(): ObjectMapper {
        return mapper
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        sessions.add(webSocket)
        print("new Connection!: ")

        /*Location l = new Location();
        l.lat = 3.123;
        l.lon = 1.452;
        publish("location", l);*/
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        try {
            val jsonNode: JsonNode = mapper.readTree(text)
            val topic: String = jsonNode.get("topic").textValue()
            requireNotNull(jsonNode.get("payload")) { "wrong json format" }
            val listenersList: ArrayList<ApiListener>? = listeners[topic]
            listenersList?.forEach { element : ApiListener ->
                try {
                    element.dataReceived(topic, jsonNode)
                } catch (e: NullPointerException) {
                    listenersList.remove(element)
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
        var listenerList: java.util.ArrayList<ApiListener>? = listeners[topic]
        if (listenerList == null) {
            listenerList = ArrayList()
            listenerList.add(listener)
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
        val listenerList: MutableList<ApiListener>? = listeners[topic]
        listenerList?.remove(listener)
    }

    /**
     * send a message to the mirror
     * @param topic topic of the message, not null
     * @param payload payload of the message, not null
     */
    override fun publish(topic: String, payload: Any) {
        val sendPackage = SendPackage()
        sendPackage.topic = topic
        sendPackage.payload = mapper.valueToTree(payload)
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
        val sendPackage = SendPackage()
        sendPackage.topic = topic
        sendPackage.payload = payload
        sessions.forEach { session: WebSocket ->
            try {
                session.send(mapper.writeValueAsString(sendPackage))
            } catch (e: IOException) {
                println(e.message)
            }
        }
    }
}