package app

import org.jetbrains.exposed.dao.*

object Users : IntIdTable(){
    val name = varchar("name",50)
    val rating = integer("rating")
}

class User(id:EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var rating by Users.rating
}

data class UserJson (val name: String,val rating:Int)