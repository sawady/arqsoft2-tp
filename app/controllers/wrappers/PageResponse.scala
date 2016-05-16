package controllers.wrappers

import play.api.libs.json._
import app.formatters.JsonModel


case class Paging (
   offset: Int,
   limit: Int,
   total: Int
)

case class PageResponse (
   items: List[JsValue],
   paging: Paging
)

object PagingJsonModel extends JsonModel[Paging] {
  val format = Json.format[Paging]
}

object PageResponseJsonModel extends JsonModel[PageResponse] {
    implicit val pagingFormat = Json.format[Paging]
    val format = Json.format[PageResponse]
}