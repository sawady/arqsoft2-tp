package repositories

import scala.annotation.implicitNotFound
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import app.formatters.JsonModel
import play.api.libs.json.Format
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json.OWrites
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.ReactiveMongoComponents
import play.modules.reactivemongo.json.JsObjectDocumentWriter
import reactivemongo.api.QueryOpts
import reactivemongo.api.ReadPreference
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection
import play.api.Logger
import play.modules.reactivemongo.json._
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import com.sun.corba.se.spi.ior.ObjectId

abstract class AbstractMongoRepository[T](val reactiveMongoApi: ReactiveMongoApi) extends ReactiveMongoComponents {

  val model: JsonModel[T]
  val colName: String
  implicit lazy val format: Format[T] = model.format

  protected def collection =
    reactiveMongoApi.db.collection[JSONCollection](colName)
   
  def create(data: T)(implicit ec: ExecutionContext): Future[T] = {
    val idObject: JsObject = Json.obj("_id" -> BSONObjectID.generate)
    val o: JsObject = Json.toJson(data).as[JsObject] ++ idObject 
    for {
        _ <- collection.insert(o).map(lastError => {
            Logger.info("Mongo LastError: %s".format(lastError))
        })
        res <- collection.find(idObject).cursor[T](ReadPreference.Primary).headOption
    }
    yield(res.get)
  }

  def delete(data: T)(implicit ec: ExecutionContext): Future[T] = {
    val o: JsObject = Json.toJson(data).as[JsObject]
    collection.remove(o).map(lastError => {
      Logger.info("Mongo LastError: %s".format(lastError))
      data
    })
  }

  def find(jsobj: JsObject, offset: Int, limit: Int)(implicit ec: ExecutionContext): Future[List[T]] =
    collection.find(jsobj).
      sort(Json.obj("$natural" -> -1)).
      options(QueryOpts((offset - 1) * limit, limit)).
      cursor[T](ReadPreference.Primary).
      collect[List](limit)

  def getById(jsObjectId: JsObject)(implicit ec: ExecutionContext): Future[Option[T]] = {
      collection.find(jsObjectId).cursor[T](ReadPreference.Primary).headOption
  }

  def all(jsobj: JsObject)(implicit ec: ExecutionContext): Future[List[T]] =
    collection.find(jsobj).
      sort(Json.obj("$natural" -> -1)).
      cursor[T](ReadPreference.Primary).
      collect[List]()

  def count(jsobj: Option[JsObject])(implicit ec: ExecutionContext): Future[Int] =
    collection.count(jsobj)

}