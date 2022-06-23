package de.hhn.aib.labsw.blackmirror.controller.API.Websockets

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * this class handles the server socket for the api.
 *
 * @author Luis Gutzeit
 * @version 23.06.2022
 */
class MasterSocketHandler(
    val url: String
) {
    /**
     * the api used by the socket
     */
    val api: MirrorApiWebsockets = MirrorApiWebsockets() {
        makeNewSocket()
    }

    private var socket: okhttp3.WebSocket? = null

    init {
        makeNewSocket()
    }

    /**
     * closes the current socket and creates a new one
     */
    private fun makeNewSocket() {
        socket?.cancel()
        socket = OkHttpClient.Builder().build()
            .newWebSocket(
                Request.Builder().url(url).build(),
                api
            )
    }
}