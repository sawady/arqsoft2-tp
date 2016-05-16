package traits

import reactivemongo.bson.BSONObjectID

trait WithId {
  val _id: Option[BSONObjectID]
}