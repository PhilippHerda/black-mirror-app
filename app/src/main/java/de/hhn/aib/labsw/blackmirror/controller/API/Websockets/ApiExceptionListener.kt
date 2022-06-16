package de.hhn.aib.labsw.blackmirror.controller.API.Websockets

interface ApiExceptionListener {
    fun exceptionReceived(t:Throwable)
}