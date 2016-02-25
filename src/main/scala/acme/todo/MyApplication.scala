package acme.todo

import acme.todo.data.mongo.MongoTodoData
import acme.todo.server.ServerAPI
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import reactivemongo.api.MongoDriver

object MyApplication extends App with ServerAPI {

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val database = {
    val driver = new MongoDriver
    val connection = driver.connection(List("192.168.99.100"))
    connection("todo")
  }

  override val todoData = new MongoTodoData(database)

  Http().bindAndHandle(route, "localhost", 8080)
}
