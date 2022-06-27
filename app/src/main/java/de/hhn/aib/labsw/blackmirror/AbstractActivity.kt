package de.hhn.aib.labsw.blackmirror

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import de.hhn.aib.labsw.blackmirror.controller.API.Websockets.*
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Abstract activity providing api access
 * @author Luis Gutzeit
 * @version 23.06.2022
 */
abstract class AbstractActivity : AppCompatActivity(), ApiListener, AutoCloseable,
    ApiExceptionListener {

    init {
        handler.api.subscribeToExceptions(this)
    }

    override fun close() {
        handler.api.unsubscribeFromExceptions(this)
    }

    /**
     * call this method to send data to the mirror
     * @param topic topic of the message, not null
     * @param payload payload of the message, not null
     */
    protected open fun publishToRemotes(topic: String, payload: JsonNode) {
        handler.api.publish(topic, payload)
    }

    /**
     * call this method to send data to the mirror
     * @param topic topic of the message, not null
     * @param payload payload of the message, not null
     */
    protected open fun publishToRemotes(topic: String, payload: Any) {
        handler.api.publish(topic, payload)
    }

    /**
     * call this method to subscribe to a topic
     * @param topic topic to subscribe, not null
     * @param listener listener to receive updates, not null
     */
    protected open fun subscribe(topic: String, listener: ApiListener) {
        handler.api.subscribe(topic, listener)
    }

    /**
     * call this method to revoke a subscription.
     * only the subscription to the given topic will be removed. All other remain active
     * @param topic topic of the subscription, not null
     * @param listener listener to be removed, not null
     */
    protected open fun unsubscribe(topic: String, listener: ApiListener) {
        handler.api.unsubscribe(topic, listener)
    }

    /**
     * use this method to convert JsonNode->class instance
     * @param node node to be converted, not null
     * @param tClass class of which the instance should be created, not null
     */
    @Throws(JsonProcessingException::class)
    protected fun <T> nodeToObject(node: JsonNode, tClass: Class<T>): T {
        return handler.api.mapper.treeToValue(node, tClass)
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
        //private val SOCKETS_URL = "ws:\\\\10.0.2.2:2306"

        private val SOCKETS_URL = "ws:\\\\blackmirror:2306"

        //private val SOCKETS_URL = "ws:\\\\LuisRechner:2306"
        val handler = MasterSocketHandler(SOCKETS_URL)

        protected val connectionAlive: Boolean
            get() {
                return handler.api.connectionAlive
            }
    }
}