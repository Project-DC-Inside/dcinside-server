package org.deepforest.dcinside.helper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeFormatHelper {
    fun from(time: LocalDateTime): String =
        time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}
