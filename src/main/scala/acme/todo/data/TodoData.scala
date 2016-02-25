package acme.todo
package data

import java.util.UUID

import acme.todo.model._

import scala.concurrent.Future

trait TodoData {
  def all: Future[Seq[TodoItem]]
  def get(id: UUID): Future[Option[TodoItem]]
  def create(item: TodoItem): Future[Unit]
  def remove(id: UUID): Future[Option[UUID]]
}

object TodoData {
  case class WriteError(msg: String) extends RuntimeException(msg)
}

