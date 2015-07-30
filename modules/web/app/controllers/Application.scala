package controllers.web

import models.web._
import play.api._
import play.api.mvc._
import play.api.Play.current

class Application extends Controller {

	def index = Action { implicit request =>
		val computers = ComputerWeb.list
		Ok(views.html.web.index("Hello! I'm the WEB!", computers))
	}

}