package controllers

import repositories.AbstractMongoRepository
import play.api.libs.json.JsValue
import scala.concurrent.Future
import play.api.mvc.Action
import play.api.mvc.BodyParsers
import scala.concurrent.ExecutionContext
import play.api.mvc.Controller
import play.api.mvc.Result
import play.api.libs.json.Format
import play.api.libs.json.OWrites
import play.api.libs.json.Json
import play.api.http.Writeable
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.json._

abstract class AbstractController[T] extends Controller {

  val repository: AbstractMongoRepository[T]
  implicit lazy val format: Format[T] = repository.format

  def JsonEndpoint(block: JsValue => Future[Result]): Action[JsValue] =
    Action.async(BodyParsers.parse.json) {
      request => block(request.body.as[JsValue])
    }

  def ModelEndpoint(block: T => Future[Result]): Action[JsValue] =
    Action.async(BodyParsers.parse.json) {
      request =>
        {
          val model = request.body.as[JsValue].validate[T]
          model.map(block).getOrElse(Future.successful(BadRequest("invalid json")))
        }
    }

  def create(): Action[JsValue] = ModelEndpoint {
    model =>
      repository.create(model).map { x => Ok(Json.toJson(x)) }
  }

  def delete(): Action[JsValue] = ModelEndpoint {
    model =>
      repository.delete(model).map { x => Ok(Json.toJson(x)) }
  }

  def all() = Action.async { request =>
    repository.all(Json.obj()).map { x => Ok(Json.toJson(x)) }
  }
  
  def find(page: Int, perPage: Int) = Action.async { request =>
    repository.find(Json.obj(), page, perPage).map { x => Ok(Json.toJson(x)) }
  }

}