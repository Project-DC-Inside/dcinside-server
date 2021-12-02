package org.deepforest.dcinside

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class DcinsideApplication

fun main(args: Array<String>) {
	runApplication<DcinsideApplication>(*args)
}
