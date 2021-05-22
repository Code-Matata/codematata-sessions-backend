package k.co.willynganga.codematatasessions

import k.co.willynganga.codematatasessions.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
open class CodematataSessionsApplication

fun main(args: Array<String>) {
	runApplication<CodematataSessionsApplication>(*args)
}
