package models

import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json.BSONFormats._
import play.api.libs.json._

case class Shop(
  _id: Option[BSONObjectID],
  latitude: Option[Double],
  longitude: Option[Double],
  name: Option[String],
  address: Option[String],
  location: Option[String])
  
object ShopJsonModel extends JsonModel[Shop] {
  val format = Json.format[Shop]
}