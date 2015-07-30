package controllers.admin

import models.admin._
import play.api._
import play.api.mvc._
import play.api.Play.current

class Application extends Controller {

	def index = Action { implicit request =>
		val computers = ComputerAdmin.list
		Ok(views.html.admin.index("Hello! I'm the ADMIN!", computers))
	}

}