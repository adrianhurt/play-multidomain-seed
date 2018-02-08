import play.api.http.DefaultHttpErrorHandler
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.routing.Router
import scala.concurrent.Future
import javax.inject.{ Singleton, Inject, Provider }

@Singleton
class ErrorHandler @Inject() (
    env: Environment,
    config: Configuration,
    sourceMapper: OptionalSourceMapper,
    router: Provider[Router],
    webErrorHandler: web.ErrorHandler,
    adminErrorHandler: admin.ErrorHandler
) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  /*
	* Gets the subdomain: "admin" o "www"
	*/
  private def getSubdomain(request: RequestHeader) = request.domain.replaceFirst("[\\.]?[^\\.]+[\\.][^\\.]+$", "")

  // 404 - page not found error
  override def onNotFound(request: RequestHeader, message: String): Future[Result] = getSubdomain(request) match {
    case "admin" => adminErrorHandler.onNotFound(request, message)
    case _       => webErrorHandler.onNotFound(request, message)
  }

  // 500 - internal server error
  override def onProdServerError(request: RequestHeader, exception: UsefulException) = getSubdomain(request) match {
    case "admin" => adminErrorHandler.onProdServerError(request, exception)
    case _       => webErrorHandler.onProdServerError(request, exception)
  }

}