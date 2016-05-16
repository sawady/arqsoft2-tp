package app.controllers

import repositories.AbstractMongoRepository
import controllers.wrappers.PageResponse
import controllers.wrappers.Paging
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
import play.api.libs.json._
import play.api.Logger
import controllers.wrappers.PageResponseJsonModel
import controllers.wrappers.PagingJsonModel
import play.api.libs.json.JsObject
import play.api.mvc.Request
import play.api.mvc.AnyContent
import traits.WithId

abstract class AbstractController[T <: WithId] extends Controller {

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
    
    def getModelPath(model: T, path: String) : String = ( path + "/" + (Json.toJson(model._id) \ "$oid").as[String] ) 

    def create(): Action[JsValue] = Action.async(BodyParsers.parse.json) {
        request => {
            val model = request.body.as[JsValue].validate[T]
            model.map(
                internalModel => repository.create(internalModel).map {
                    x => Created(toWeb(Json.toJson(x))).withHeaders( "location" -> getModelPath(x, request.path) )
                }
            ).getOrElse(Future.successful(BadRequest("invalid json")))
        }
    }

    def delete(): Action[JsValue] = ModelEndpoint {
        model =>
            repository.delete(model).map { x => Ok(Json.toJson(x)) }
    }

    def all() = Action.async {
        request =>
            repository.all(Json.obj()).map { x => Ok(Json.toJson(x)) }
    }
    
    def requestParamsToJsObject(request: Request[AnyContent]): JsObject = {
        var requestJson: JsObject = Json.obj()
        for ((k,v) <- request.queryString.map { case (k, v) => k -> v.mkString }) {
            var jsonObject: JsObject = null
            try {
                jsonObject = Json.obj(k -> v.toDouble)
            } catch { 
                case _ => jsonObject = Json.obj(k -> v) 
            }
            requestJson = requestJson ++ jsonObject
        }
        requestJson
    }
    
    def toWeb(js: JsValue): JsValue = {
        val picId = js.transform((__ \ '_id \ '$oid).json.pick).get
        
        val changedJs = js.transform(
            (__).json.update(
                __.read[JsObject].map{ o => o ++ Json.obj( "id" -> picId ) } 
            )
        ).get
        
        val erasedJs = changedJs.transform((__ \ '_id).json.prune).get
        
        return erasedJs 
    }
    
    /*
     def fromWeb(js: JsValue): JsValue = {
        val tr = (__ \ '_id).json.update(
        __.read[String].map { v => JsObject(("$oid", JsString(v)) +: Nil) })
        
        return js.transform(tr).asOpt match {
        case Some(js2) => js2
        case None => js
        }
    }
    */

    def find(offset: Int, limit: Int) = Action.async {
        request =>
            var requestJsObject = requestParamsToJsObject(request)
            implicit val pagingFormat = PagingJsonModel.format
            implicit val pageResponseFormat = PageResponseJsonModel.format
            for {
                items <- repository.find(requestJsObject, offset, limit)
                total <- repository.count(Some(Json.obj()))
            } yield {
                var paging = new Paging(offset, limit, total)
                Ok(Json.toJson(PageResponse(items.map(x => toWeb(Json.toJson(x))), paging)))
            }
    }
    
    def findById(id: String) = Action.async {
        request =>
            repository.getById(Json.obj("_id" -> Json.obj("$oid" -> id))).map { x => Ok(toWeb(Json.toJson(x))) }
    }

}