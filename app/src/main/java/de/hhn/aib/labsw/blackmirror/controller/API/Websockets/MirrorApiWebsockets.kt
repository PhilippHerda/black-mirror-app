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

    override fun init() {
        //no code atm
    }

    override fun finish() {
        //no code atm
    }

    fun getJSONMapper(): ObjectMapper {
        return mapper
    }

    override fun onOpen(session: WebSocket, response: Response) {
        sessions.add(session)
        print("new Connection!: ")

        /*Location l = new Location();
        l.lat = 3.123;
        l.lon = 1.452;
        publish("location", l);*/
    }

    override fun onMessage(session: WebSocket, message: String) {
        try {
            val jsonNode: JsonNode = mapper.readTree(message)
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

    override fun onClosed(session: WebSocket, code: Int, reason: String) {
        // WebSocket connection closes
        print("Connection closed: ")
        sessions.remove(session)
    }

    override fun onFailure(session: WebSocket, ex: Throwable, response: Response?) {
        session.close(1013, "try again later")
        sessions.remove(session)
    }

    override fun subscribe(topic: String, listener: ApiListener) {
        var listenerList: ArrayList<ApiListener>? = listeners[topic]
        if (listenerList == null) {
            listenerList = ArrayList()
            listenerList!!.add(listener)
            listeners[topic] = listenerList
        } else {
            listenerList.add(listener)
        }
    }

    override fun unsubscribe(topic: String, listener: ApiListener) {
        TODO("Not yet implemented")
    }

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