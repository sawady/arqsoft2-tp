package app.controllers

import scala.concurrent.Future
import javax.inject.Inject
import play.api.mvc.Action
import play.api.mvc.Controller
import repositories.PriceRepository
import play.api.libs.json.JsValue
import models.Price
import play.api.libs.json.Json
import models.PriceJsonModel
import play.api.libs.json.Format
import play.api.libs.json.OWrites
import play.api.http.Writeable

class PriceController @Inject() (val priceRepository: PriceRepository) extends AbstractController[Price] {
  
  override val repository = priceRepository
  
}