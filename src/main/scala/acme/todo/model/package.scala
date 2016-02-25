package acme.todo

import java.util.UUID

package object model {
  case class TodoItem(id: UUID, description: String, done: Boolean)
}
