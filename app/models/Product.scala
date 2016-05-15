package models

import app.formatters.JsonModel
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json.BSONFormats._
import play.api.libs.json._

case class Product(
	product_id: String,
    name: Option[String]
)
  
object ProductJsonModel extends JsonModel[Product] {
  val format = Json.format[Product]
}