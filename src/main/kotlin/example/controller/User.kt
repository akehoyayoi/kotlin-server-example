package example.controller

/**
 * Created by yoheiokaya on 2018/01/03.
 */

import example.model.view.*
import io.ktor.routing.*
import io.ktor.locations.*
import io.ktor.application.*
import io.ktor.request.receive
import io.ktor.response.respond
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.transactions.transaction

@location("/users")
class Users {

    @location("/{id}")
    data class Id(val id: Int)
}

fun Route.users() {


    post<Users> {
        val createUser = call.receive<CreateUser>()
        val user = transaction {
            logger.addLogger(StdOutSqlLogger)
            example.model.database.User.new {
                name = createUser.name
                age = createUser.age
            }
        }
        call.respond(ViewUser(user))
    }

    get<Users> {
        val viewUsers = transaction {
            logger.addLogger(StdOutSqlLogger)
            // all() method return lazy iterator, so it must map view model in transaction
            example.model.database.User.all().map { ViewUser(it) }
        }
        call.respond(viewUsers)
    }

    get<Users.Id> {
        val user = transaction {
            logger.addLogger(StdOutSqlLogger)
            example.model.database.User.findById(it.id)
        }
        call.respond(ViewUser(user!!))
    }

    put<Users.Id> {
        val putUser = call.receive<PutUser>()
        val user = transaction {
            logger.addLogger(StdOutSqlLogger)
            val u = example.model.database.User.findById( it.id )
            u?.name = putUser.name
            u?.age = putUser.age
            u
        }
        call.respond(ViewUser(user!!))
    }

}