package models

import app.formatters.JsonModel
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json.BSONFormats._
import play.api.libs.json._
import traits.WithId

case class Shop(
  _id: Option[BSONObjectID],
  latitude: Option[Double],
  longitude: Option[Double],
  name: Option[String],
  address: Option[String],
  location: Option[String]
) extends WithId
  
object ShopJsonModel extends JsonModel[Shop] {
  val format = Json.format[Shop]
}