package repositories

import javax.inject.Inject
import play.modules.reactivemongo.ReactiveMongoApi
import models.Shop
import models.JsonModel
import play.api.libs.json.Json
import models.ShopJsonModel
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject() (override val reactiveMongoApi: ReactiveMongoApi) extends AbstractMongoRepository[Shop](reactiveMongoApi) {

  val model = ShopJsonModel
  val colName = "shops"

}