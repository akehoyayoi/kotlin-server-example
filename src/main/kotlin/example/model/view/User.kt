package example.model.view

/**
 * Created by yoheiokaya on 2018/01/03.
 */


data class CreateUser(val name: String, val age: Int)
data class PutUser(val name: String, val age: Int)

data class ViewUser(val id: Int, val name: String, val age: Int) {

    constructor(user: example.model.database.User): this(user.id.value, user.name, user.age)
}