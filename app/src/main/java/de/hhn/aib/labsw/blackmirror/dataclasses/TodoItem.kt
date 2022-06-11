package de.hhn.aib.labsw.blackmirror.dataclasses

import java.time.ZonedDateTime

data class TodoItem(
    var text: String,
    val date: ZonedDateTime
)
