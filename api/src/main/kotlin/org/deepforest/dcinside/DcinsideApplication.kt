package org.deepforest.dcinside

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class DcinsideApplication

fun main(args: Array<String>) {
	runApplication<DcinsideApplication>(*args)
}
