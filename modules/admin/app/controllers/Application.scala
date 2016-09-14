package controllers.admin

import models.admin._
import play.api._
import play.api.mvc._
import play.api.i18n.{ I18nSupport, MessagesApi, Messages, Lang }
import net.ceedubs.ficus.Ficus._
import javax.inject.{ Inject, Singleton }

@Singleton
class Application @Inject() (val messagesApi: MessagesApi, conf: Configuration) extends Controller with I18nSupport {

  def index = Action { implicit request =>
    val computers = ComputerAdmin.list
    Ok(views.html.admin.index(Messages("admin.subtitle"), computers, configThisFile = conf.underlying.as[String]("this.file")))
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