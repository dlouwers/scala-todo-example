package acme.todo.server

import java.util.UUID
import spray.json._

import acme.todo.model._

trait APIJsonProtocol extends DefaultJsonProtocol {
  implicit object UuidJsonFormat extends JsonFormat[UUID] {
    def write(x: UUID) = JsString(x toString ())
    def read(value: JsValue) = value match {
      case JsString(x) => UUID.fromString(x)
      case x => deserializationError("Expected UUID as JsString, but got " + x)
    }
  }
  implicit val itemFormat = jsonFormat3(TodoItem)
}
