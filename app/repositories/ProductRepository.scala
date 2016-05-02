package repositories

import javax.inject.Inject
import play.modules.reactivemongo.ReactiveMongoApi
import models.Product
import models.JsonModel
import play.api.libs.json.Json
import models.ProductJsonModel
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject() (override val reactiveMongoApi: ReactiveMongoApi) extends AbstractMongoRepository[Product](reactiveMongoApi) {

  val model = ProductJsonModel
  val colName = "products"

}