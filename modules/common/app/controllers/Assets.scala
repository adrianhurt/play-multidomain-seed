package controllers.common

import play.api.mvc._
import play.api.http.DefaultHttpErrorHandler
import play.api.Configuration
import net.ceedubs.ficus.Ficus._
import controllers.{ AssetsBuilder, AssetsMetadata }
import controllers.Assets.Asset

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.io.File

class Assets(errorHandler: DefaultHttpErrorHandler, assetsMetadata: AssetsMetadata) extends AssetsBuilder(errorHandler, assetsMetadata) {
  def public(path: String, file: Asset) = versioned(path, file)
  def lib(path: String, file: Asset) = versioned(path, file)
  def css(path: String, file: Asset) = versioned(path, file)
  def commonCss(path: String, file: Asset) = versioned(path, file)
  def js(path: String, file: Asset) = versioned(path, file)
  def commonJs(path: String, file: Asset) = versioned(path, file)
  def img(path: String, file: Asset) = versioned(path, file)
  def commonImg(path: String, file: Asset) = versioned(path, file)
}

/*
* Shared resources between subprojects. The base path is defined by the "rsc.folder" in the conf file.
* It's an extremely simpliflied version of the code from Assets.scala (https://github.com/playframework/playframework/blob/2.4.x/framework/src/play/src/main/scala/play/api/controllers/Assets.scala)
*/
abstract class SharedResources(errorHandler: DefaultHttpErrorHandler, conf: Configuration) extends InjectedController {
  private lazy val path = conf.underlying.as[String]("rsc.folder")

  def rsc(filename: String) = Action.async { implicit request =>
    {
      val file = new File(path + filename)
      if (file.exists() && file.isFile())
        Future.successful(Ok.sendFile(file))
      else
        errorHandler.onClientError(request, NOT_FOUND, "File not found")
    }.recoverWith {
      case e => errorHandler.onServerError(request, new RuntimeException(s"Unexpected error while serving $filename at $path: " + e.getMessage, e))
    }
  }
}
