package de.hhn.aib.labsw.blackmirror.controller.API.Websockets

import java.lang.RuntimeException

class ApiException:RuntimeException {
    constructor(t:Throwable):super(t)
    constructor(text:String):super(text)
}