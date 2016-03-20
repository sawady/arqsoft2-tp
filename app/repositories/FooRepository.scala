package repositories

import play.modules.reactivemongo.ReactiveMongoApi
import javax.inject.Inject
import javax.inject.Singleton
import reactivemongo.play.json.collection.JSONCollection
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import reactivemongo.api.ReadPreference
import play.modules.reactivemongo.ReactiveMongoComponents
import play.api.libs.json.JsArray
import play.api.Logger

@Singleton
class FooRepository @Inject() (
    val reactiveMongoApi: ReactiveMongoApi) extends ReactiveMongoComponents {

  // BSON-JSON conversions
  import play.modules.reactivemongo.json._

  protected def collection =
    reactiveMongoApi.db.collection[JSONCollection]("foos")

  def find()(implicit ec: ExecutionContext): Future[JsArray] =
    collection.find(Json.obj()).cursor[JsObject](ReadPreference.Primary).collect[List]().map { x => JsArray(x) }

  def save(name: String, age: Int)(implicit ec: ExecutionContext): Future[JsObject] = {
    val json = Json.obj(
      "name" -> name,
      "age" -> age,
      "created" -> new java.util.Date().getTime())

    collection.insert(json).map(lastError => {
      Logger.info("Mongo LastError: %s".format(lastError))
      json      
    })
  }

}