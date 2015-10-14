package controllers.admin

import models.admin._
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.i18n.{ I18nSupport, MessagesApi, Messages, Lang }
import javax.inject.Inject

class Application @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index = Action { implicit request =>
    val computers = ComputerAdmin.list
    Ok(views.html.admin.index(Messages("admin.subtitle"), computers))
  }

  def selectLang(lang: String) = Action { implicit request =>
    Logger.logger.debug("Change user lang to : " + lang)
    request.headers.get(REFERER).map { referer =>
      Redirect(referer).withLang(Lang(lang))
    }.getOrElse {
      Redirect(routes.Application.index).withLang(Lang(lang))
    }
  }

}