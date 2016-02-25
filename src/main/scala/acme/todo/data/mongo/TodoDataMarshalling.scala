package acme.todo
package data
package mongo

import java.util.UUID
import reactivemongo.bson._

import model.TodoItem
import acme.todo.util._

trait TodoDataMarshalling {
  implicit object UUIDBsonHandler extends BSONHandler[BSONBinary, UUID] {
    override def write(uuid: UUID): BSONBinary = BSONBinary(uuid.toBytes, Subtype.UuidSubtype)
    override def read(bson: BSONBinary): UUID = bson match {
      case BSONBinary(value, Subtype.UuidSubtype) => uuidFromBytes(value.readArray(value.size))
    }
  }

  implicit val todoItemReader: BSONDocumentReader[TodoItem] = Macros.reader[TodoItem]
  implicit val todoItemWriter: BSONDocumentWriter[TodoItem] = Macros.writer[TodoItem]
}
