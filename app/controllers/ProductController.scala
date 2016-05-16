package app.controllers

import scala.concurrent.Future
import javax.inject.Inject
import play.api.mvc.Action
import play.api.mvc.Controller
import repositories.ProductRepository
import play.api.libs.json.JsValue
import models.Product
import play.api.libs.json.Json
import models.ProductJsonModel
import play.api.libs.json.Format
import play.api.libs.json.OWrites
import play.api.http.Writeable
import play.api.libs.json.JsObject
import play.api.libs.json._

class ProductController @Inject() (val productRepository: ProductRepository) extends AbstractController[Product] {
  
  override val repository = productRepository
  
  override def toWeb(js: JsValue): JsValue = {
        val erasedJs = js.transform((__ \ '_id).json.prune).get
        return erasedJs 
    }
    
}