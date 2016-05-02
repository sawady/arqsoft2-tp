package models

import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json.BSONFormats._
import play.api.libs.json._
import org.joda.time.DateTime

case class Price(
	_id: BSONObjectID,
	product_id : BSONObjectID,
	product : Option[Product],
	shop_id: BSONObjectID,
	shop: Option[Shop],
	datetime: Option[DateTime],
	price: Double
)
  
object PriceJsonModel extends JsonModel[Price] {
    implicit val shopFormat = Json.format[Shop]
    implicit val productFormat = Json.format[Product]
    val format = Json.format[Price]
}