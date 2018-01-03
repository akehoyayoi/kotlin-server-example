package example.model.database

/**
 * Created by yoheiokaya on 2018/01/03.
 */

import org.jetbrains.exposed.dao.*

object Users : IntIdTable() {
    val name = varchar("name", 50).index()
    val age = integer("age")
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    var name by Users.name
    var age by Users.age
}
