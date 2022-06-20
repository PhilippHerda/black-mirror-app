package de.hhn.aib.labsw.blackmirror

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import de.hhn.aib.labsw.blackmirror.controller.API.Websockets.ApiExceptionListener
import de.hhn.aib.labsw.blackmirror.controller.API.Websockets.ApiListener
import de.hhn.aib.labsw.blackmirror.controller.API.Websockets.MirrorApi
import de.hhn.aib.labsw.blackmirror.controller.API.Websockets.MirrorApiWebsockets
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Abstract activity providing api access
 * @author Luis Gutzeit
 * @version 16.06.2022
 */
abstract class AbstractActivity : AppCompatActivity(), ApiListener, AutoCloseable,
    ApiExceptionListener {

    init {
        api.subscribeToExceptions(this)
    }

    override fun close() {
        api.unsubscribeFromExceptions(this)
    }

    /**
     * call this method to send data to the mirror
     * @param topic topic of the message, not null
     * @param payload payload of the message, not null
     */
    protected open fun publishToRemotes(topic: String, payload: JsonNode) {
        api.publish(topic, payload)
    }

    /**
     * call this method to send data to the mirror
     * @param topic topic of the message, not null
     * @param payload payload of the message, not null
     */
    protected open fun publishToRemotes(topic: String, payload: Any) {
        api.publish(topic, payload)
    }

    /**
     * call this method to subscribe to a topic
     * @param topic topic to subscribe, not null
     * @param listener listener to receive updates, not null
     */
    protected open fun subscribe(topic: String, listener: ApiListener) {
        api.subscribe(topic, listener)
    }

    /**
     * call this method to revoke a subscription.
     * only the subscription to the given topic will be removed. All other remain active
     * @param topic topic of the subscription, not null
     * @param listener listener to be removed, not null
     */
    protected open fun unsubscribe(topic: String, listener: ApiListener) {
        api.unsubscribe(topic, listener)
    }

    /**
     * use this method to convert JsonNode->class instance
     * @param node node to be converted, not null
     * @param tClass class of which the instance should be created, not null
     */
    @Throws(JsonProcessingException::class)
    protected fun <T> nodeToObject(node: JsonNode, tClass: Class<T>): T {
        return api.mapper.treeToValue(node, tClass)
    }

    /**
     * override this method to handle new data received
     * must be overriden when at least one topic is subscibed
     * @param topic the topic of the package that was received, not null
     * @param `object` the node containing the payload, not null
     */
    override fun dataReceived(topic: String, `object`: JsonNode) {}

    /**
     * override this method the handle errors in the api
     *
     * @param t the exception that caused the error
     */
    override fun exceptionReceived(t: Throwable) {

    }

    /**
     * companion object holding reference to WebsocketServer
     */
    companion object {
        //set URL here
        //10.0.2.2 is localhost of the computer running the emulator
        //private val SOCKETS_URL = "ws:\\\\10.0.2.2"               // use this for debugging and set PORT on mirror side (in MirrorApiWebsockets) to 80

        private val SOCKETS_URL = "ws:\\\\blackmirror:2306"

        //create the apiListener and create a socket
        private var api = MirrorApiWebsockets()
        protected val connectionAlive: Boolean
            get() {
                return api.connectionAlive
            }
        private var socket = OkHttpClient.Builder().build()
            .newWebSocket(Request.Builder().url(SOCKETS_URL).build(), api)

        fun apiLostConnection() {
            api = MirrorApiWebsockets()
            socket = OkHttpClient.Builder().build()
                .newWebSocket(Request.Builder().url(SOCKETS_URL).build(), api)
        }
    }
}