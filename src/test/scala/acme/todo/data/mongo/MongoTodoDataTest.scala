package acme.todo
package data.mongo

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global

import org.scalatest._
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import reactivemongo.api.MongoDriver

import acme.todo.model.TodoItem

class MongoTodoDataTest extends WordSpec with Matchers with ScalaFutures with IntegrationPatience {
  "MongoTodoData" should {

    class Context {
      val unknownUuid = UUID.fromString("1488BD23-DAF1-45D8-8605-ACE4EB1AA3E6")
      val uuid = UUID.fromString("AA0E51C8-2F43-43D7-8E8B-DE4F824FED3C")
      val item = TodoItem(uuid, "This is a task", done = false)

      val database = {
        val driver = new MongoDriver
        val connection = driver.connection(List("192.168.99.100"))
        connection("todo")
      }
      val subject = new MongoTodoData(database)
    }

    "create a todo item" in new Context {
      subject.create(item).futureValue shouldEqual ()
      subject.get(uuid).futureValue shouldEqual Some(item)
    }

    "update a todo item" in new Context {
      subject.create(item.copy(done = true)).futureValue shouldEqual ()
      subject.get(uuid).futureValue shouldEqual Some(item.copy(done = true))
    }

    "not find a non-existing todo item" in new Context {
      subject.get(unknownUuid).futureValue shouldEqual None
    }

    "delete a todo item" in new Context {
      subject.create(item).futureValue shouldEqual ()
      subject.remove(item.id).futureValue shouldEqual Some(item.id)
    }

    "not delete a non-existing todo item" in new Context {
      subject.remove(unknownUuid).futureValue shouldEqual None
    }
  }
}
