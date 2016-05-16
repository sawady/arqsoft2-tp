package models

import app.formatters.JsonModel
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json.BSONFormats._
import play.api.libs.json._
import org.joda.time.DateTime
import traits.WithId

case class Price(
    _id: Option[BSONObjectID],
	product_id : String,
	product : Option[Product],
	shop_id: BSONObjectID,
	shop: Option[Shop],
	datetime: Option[DateTime],
	price: Double
) extends WithId
  
object PriceJsonModel extends JsonModel[Price] {
    implicit val shopFormat = Json.format[Shop]
    implicit val productFormat = Json.format[Product]
    val format = Json.format[Price]
}