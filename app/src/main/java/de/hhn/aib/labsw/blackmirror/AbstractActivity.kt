package de.hhn.aib.labsw.blackmirror

import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import de.hhn.aib.labsw.blackmirror.controller.API.Websockets.ApiListener
import de.hhn.aib.labsw.blackmirror.controller.API.Websockets.MirrorApi
import de.hhn.aib.labsw.blackmirror.controller.API.Websockets.MirrorApiWebsockets


abstract class AbstractActivity: AppCompatActivity(),ApiListener {
    private val api = MirrorApiWebsockets.getInstance()

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
}