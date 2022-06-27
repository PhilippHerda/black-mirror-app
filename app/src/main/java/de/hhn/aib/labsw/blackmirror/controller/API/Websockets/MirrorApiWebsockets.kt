package de.hhn.aib.labsw.blackmirror.controller.API.Websockets

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

/**
 * Implementation of the interface using websockets
 * @author Luis Gutzeit
 * @version 05.05.2022
 */
class MirrorApiWebsockets(
    val resetConnection: () -> Unit
) : WebSocketListener(), MirrorApi {
    private val sessions = mutableListOf<WebSocket>()
    private val listeners = mutableMapOf<String, MutableList<ApiListener>>()
    private val errorListeners = mutableListOf<ApiExceptionListener>()
    private var backgroundTask = CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
        while (true) {
            if (connectionAlive) {
                delay(5 * 1000)
                sessions.forEach {
                    it.send("alive?")
                }
            }
        }
    }

    /**
     * this property holds the status of the connection to the mirror.
     * true means the connection is alive.
     * false means no connection to the mirror is active.
     * The mirror API will automaticall try to reconnect every 10 seconds
     */
    var connectionAlive = true
        private set

    val mapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    /**
     * system method - do not call this method by yourself!!
     */
    override fun onOpen(webSocket: WebSocket, response: Response) {
        sessions.add(webSocket)
        connectionAlive = true
    }

    /**
     * system method - do not call this method by yourself!!
     */
    override fun onMessage(webSocket: WebSocket, text: String) {
        try {
            val jsonNode: JsonNode = mapper.readTree(text)
            val topic: String = jsonNode.get("topic").textValue()
            requireNotNull(jsonNode.get("payload")) { "wrong json format" }
            val listenersList = listeners[topic]
            listenersList?.forEach { e ->
                e.dataReceived(topic, jsonNode.get("payload"))
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    /**
     * system method - do not call this method by yourself!!
     */
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        // WebSocket connection closes
        print("Connection closed: ")
        sessions.remove(webSocket)
        if (sessions.isEmpty()) connectionAlive = false
    }

    /**
     * system method - do not call this method by yourself!!
     */
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        webSocket.close(1013, "try again later")
        if (
            t is IOException
        ) {
            backgroundTask.cancel()
            connectionAlive = false
            CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                delay(10 * 1000) //wait 10 seconds before retry
                resetConnection()
            }
        }
        for (errorListener in errorListeners) {
            errorListener.exceptionReceived(t)
        }
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
        val listenerList = listeners[topic]
        listenerList?.remove(listener)
    }

    /**
     * send a message to the mirror
     * @param topic topic of the message, not null
     * @param payload payload of the message, not null
     *
     * @return true if the message could be queued to at least one receiver, else false
     */
    override fun publish(topic: String, payload: Any): Boolean {
        val sendPackage = SendPackage(topic, mapper.valueToTree(payload))
        if (connectionAlive) {
            var oneSuccess = false
            sessions.forEach { session: WebSocket ->
                if (session.send(mapper.writeValueAsString(sendPackage))) oneSuccess = true
            }
            return oneSuccess
        } else return false
    }

    /**
     * send a message to the mirror
     * @param topic topic of the message, not null
     * @param payload payload of the message, not null
     *
     * @return true if the message could be queued to at least one receiver, else false
     */
    override fun publish(topic: String, payload: JsonNode): Boolean {
        val sendPackage = SendPackage(topic, payload)
        var oneSuccess = false
        if (connectionAlive) {
            sessions.forEach { session: WebSocket ->
                if (session.send(mapper.writeValueAsString(sendPackage))) oneSuccess = true
            }
            return oneSuccess
        } else return false
    }

    /**
     * subscribe to new exceptions
     * @param listener the listener to subscribe to exceptions in MirrorApi
     */
    override fun subscribeToExceptions(listener: ApiExceptionListener) {
        errorListeners.add(listener)
    }

    /**
     * unsubscribe from new exceptions
     * @param listener the listener to unsubscribe
     */
    override fun unsubscribeFromExceptions(listener: ApiExceptionListener) {
        errorListeners.remove(listener)
    }
}