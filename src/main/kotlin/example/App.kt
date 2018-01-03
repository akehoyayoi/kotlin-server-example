package example

/**
 * Created by yoheiokaya on 2017/12/23.
 */

import example.controller.users
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.locations.*

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create

data class Item(val id: Int, val name: String)

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Locations)

    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }

    install(Routing) {
        users()
    }
}

fun main() {
    Database.connect("jdbc:h2:./test", driver = "org.h2.Driver")
    transaction {
        logger.addLogger(StdOutSqlLogger)
        create(example.model.database.Users)
    }
    embeddedServer(Netty, 8080, watchPaths = listOf("AppKt"), module = Application::module).start()
}
