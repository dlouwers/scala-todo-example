package acme.todo
package data
package mongo

import java.util.UUID

import scala.concurrent._

import reactivemongo.api.commands._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson._
import reactivemongo.api._

import model._
import acme.todo.util._

class MongoTodoData(db: DB)(implicit ec: ExecutionContext) extends TodoData with TodoDataMarshalling {

  val collection: BSONCollection = db("todo")

  override def all: Future[Seq[TodoItem]] = collection.find(BSONDocument()).cursor[TodoItem]().collect[Seq]()

  override def get(id: UUID): Future[Option[TodoItem]] = {
    val query = BSONDocument("_id" -> BSONBinary(id.toBytes, Subtype.UuidSubtype))
    collection.find(query).one[TodoItem]
  }

  override def remove(id: UUID): Future[Option[UUID]] = {
    val query = BSONDocument("_id" -> BSONBinary(id.toBytes, Subtype.UuidSubtype))
    collection.remove(query).map {
      case r: WriteResult if r.ok && r.n == 1 => Some(id)
      case r: WriteResult if r.ok && r.n == 0 => None
      case r: WriteResult => throw TodoData.WriteError(s"Error removing item with id: $id")
    }
  }
  override def create(item: TodoItem): Future[Unit] = {
    val query = BSONDocument("_id" -> BSONBinary(item.id.toBytes, Subtype.UuidSubtype))
    collection.update(query, item, upsert = true).map {
      case r: UpdateWriteResult if r.ok => ()
      case r: UpdateWriteResult => throw TodoData.WriteError(s"Error while writing item: $item")
    }
  }
}
