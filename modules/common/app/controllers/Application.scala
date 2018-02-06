package controllers.common

import play.api._
import play.api.mvc._

class Application extends InjectedController {

  def status = Action {
    Ok("Everything is great!")
  }

}