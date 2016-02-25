package acme.todo

import java.nio.ByteBuffer
import java.util.UUID

package object util {
  implicit class UUIDPlus(uuid: UUID) {
    def toBytes: Array[Byte] = {
      val bb: ByteBuffer  = ByteBuffer.wrap(new Array[Byte](16))
      bb.putLong(uuid.getMostSignificantBits)
      bb.putLong(uuid.getLeastSignificantBits)
      bb.array()
    }
  }

  def uuidFromBytes(bytes: Array[Byte]): UUID = {
    val bb: ByteBuffer  = ByteBuffer.wrap(bytes)
    val most = bb.getLong
    val least = bb.getLong
    new UUID(most, least)
  }
}
