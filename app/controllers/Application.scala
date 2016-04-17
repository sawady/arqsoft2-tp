package controllers

import scala.concurrent.Future
import javax.inject.Inject
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
