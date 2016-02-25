package acme.todo
package server

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContext.Implicits.global

import data.TodoData
import model._

import scala.concurrent.Future

trait ServerAPI extends SprayJsonSupport with APIJsonProtocol {

  def todoData: TodoData
  
  def provideIfDefined[T](value: Future[Option[T]]): Directive1[T] = {
    onSuccess(value).flatMap {
      case Some(v) => provide(v)
      case None => reject
    }
  }

  val route = pathPrefix("api") {
    path("todos") {
      get {
        complete {
          todoData.all
        }
      }
    } ~
    path("todo" / JavaUUID) { id =>
      delete {
        provideIfDefined(todoData.remove(id)) { result =>
          complete(HttpResponse(204))
        }
      } ~ put {
        entity(as[TodoItem]) { item =>
          complete {
            todoData.create(item).map(_ => HttpResponse(204))
          }
        }
      } ~ get {
        provideIfDefined(todoData.get(id)) { result =>
          complete(result)
        }
      }
    }
  }
}
