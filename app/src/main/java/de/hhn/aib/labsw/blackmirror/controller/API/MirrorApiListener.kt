package de.hhn.aib.labsw.blackmirror.controller.API

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.IOException

class MirrorApiListener : WebSocketListener() {

    private var sessions: ArrayList<WebSocket> = ArrayList()
    private var mapper: ObjectMapper = ObjectMapper()

    // HashMap holding topics and all TopicListeners subscribed to them
    private var listeners: HashMap<String, ArrayList<TopicListener>?> =
        HashMap()

    /**
     * Returns the APIs instance of the ObjectMapper
     * which can be used to turn JsonNodes (from dataReceived() method)
     * back into Objects and vice versa
     */
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
            val listenersList: ArrayList<TopicListener>? = listeners[topic]
            listenersList?.forEach { element : TopicListener ->
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

    /**
     * This method is used to subscribe a Class/Activity to the specified topic
     * Usually the given TopicListener should be "this"
     */
    fun subscribe(topic: String, listener: TopicListener) {
        var listenerList: ArrayList<TopicListener>? = listeners[topic]
        if (listenerList == null) {
            listenerList = ArrayList()
            listenerList!!.add(listener)
            listeners[topic] = listenerList
        } else {
            listenerList.add(listener)
        }
    }

    /**
     * This method sends any Object to anyone subscribed to the specified topic
     */
    fun publish(topic: String, payload: Any?) {
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
     * This method sends a JsonNode to anyone subscribed to the specified topic
     */
    fun publish(topic: String, payload: JsonNode) {
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

    /**
     * Singleton Pattern is used for instantiation
     */
    companion object {
        private var instance: MirrorApiListener? = null
        fun getInstance(): MirrorApiListener? {
            if (instance == null) {
                instance = MirrorApiListener()
            }
            return instance
        }
    }

    init {
        instance = this
    }
}