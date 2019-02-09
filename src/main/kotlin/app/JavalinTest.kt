package app



import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object JavalinTest {
    @JvmStatic
    fun main(args: Array<String>) {
        InitDb()
        val app = Javalin.create().apply {
            port(7070)
            ws("/chat") { ws ->
                ws.onConnect { session ->
                    println("Connected")
                }
                ws.onClose { session, status, message ->
                    println("Disconnected")
                }
                ws.onMessage { session, message ->
                    session.remote.sendString(message)
                }
            }
        }.start()


        app.routes {
            get("/createuser/:name"){ctx ->
                 transaction {
                     addLogger(StdOutSqlLogger)
                     User.new {
                         name = ctx.pathParam("name")
                         rating = 0
                     }
                 }
            }
            post("/users") { ctx ->
               // val userJson = ctx.body<UserJson>()

                ctx.status(204)
            }
        }
    }

    fun InitDb(){
        val hikariConfig = HikariConfig("/hikari.properties")
        val ds = HikariDataSource(hikariConfig)
        Database.connect(ds)
    }
}

