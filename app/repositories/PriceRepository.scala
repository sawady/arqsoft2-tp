package repositories

import javax.inject.Inject
import play.modules.reactivemongo.ReactiveMongoApi
import models.Price
import app.formatters.JsonModel
import play.api.libs.json.Json
import models.PriceJsonModel
import javax.inject.Singleton

@Singleton
class PriceRepository @Inject() (override val reactiveMongoApi: ReactiveMongoApi) extends AbstractMongoRepository[Price](reactiveMongoApi) {

  val model = PriceJsonModel
  val colName = "prices"

}