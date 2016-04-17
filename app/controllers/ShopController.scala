package controllers

import scala.concurrent.Future
import javax.inject.Inject
import play.api.mvc.Action
import play.api.mvc.Controller
import repositories.ShopRepository
import play.api.libs.json.JsValue
import models.Shop
import play.api.libs.json.Json
import models.ShopJsonModel
import play.api.libs.json.Format
import play.api.libs.json.OWrites
import play.api.http.Writeable

class ShopController @Inject() (val shopRepository: ShopRepository) extends AbstractController[Shop] {
  
  override val repository = shopRepository
  
}