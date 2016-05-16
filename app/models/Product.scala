package models

import app.formatters.JsonModel
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json.BSONFormats._
import play.api.libs.json._
import traits.WithId

case class Product(
    _id: Option[BSONObjectID],
	product_id: Long,
    name: Option[String]
) extends WithId
  
object ProductJsonModel extends JsonModel[Product] {
  val format = Json.format[Product]
}