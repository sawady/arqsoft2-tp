package controllers

import scala.concurrent.Future
import javax.inject.Inject
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller
import repositories.FooRepository

class Application @Inject() (val fooRepository: FooRepository)
    extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def create(name: String, age: Int) = Action.async {
    fooRepository.save(name, age).map(msg =>
      Ok(msg))
  }

  def getAllFoos = Action.async {
    fooRepository.find().map { items =>
      Ok(items)
    }
  }

}
