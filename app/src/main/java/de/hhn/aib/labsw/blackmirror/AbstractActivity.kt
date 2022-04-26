package de.hhn.aib.labsw.blackmirror

import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import de.hhn.aib.labsw.blackmirror.controller.API.Websockets.ApiListener
import de.hhn.aib.labsw.blackmirror.controller.API.Websockets.MirrorApi
import de.hhn.aib.labsw.blackmirror.controller.API.Websockets.MirrorApiWebsockets
import okhttp3.OkHttpClient
import okhttp3.Request


abstract class AbstractActivity: AppCompatActivity(),ApiListener {
    //set URL here
    //10.0.2.2 is localhost of the computer running the emulator
    val SOCKETS_URL = "ws:\\\\10.0.2.2"

    //create the apiListener and create a socket
    val api = MirrorApiWebsockets()
    private val socket = OkHttpClient.Builder().build().newWebSocket(Request.Builder().url(SOCKETS_URL).build(),api)

    protected open fun publish(topic:String, payload: JsonNode){
        api.publish(topic,payload)
    }

    protected open fun publish(topic:String, payload:Any){
        api.publish(topic,payload)
    }

    protected open fun subscribe(topic:String, listener: ApiListener){
        api.subscribe(topic,listener)
    }

    protected open fun unsubscribe(topic:String, listener: ApiListener){
        api.unsubscribe(topic,listener)
    }

    @Throws(JsonProcessingException::class)
    protected fun <T> nodeToObject(node: JsonNode?, tClass: Class<T>?): T {
        return api.getJSONMapper().treeToValue(node, tClass)
    }

    override fun dataReceived(topic: String, `object`: JsonNode) {}

    override fun onDestroy() {
        socket.close(1000,"session terminated")
        super.onDestroy()
    }
}