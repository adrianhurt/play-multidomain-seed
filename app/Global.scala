import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.Future

object Global extends GlobalSettings {
	
	private def getSubdomain (request: RequestHeader) = request.domain.replaceFirst("[\\.]?[^\\.]+[\\.][^\\.]+$", "")
	
	override def onRouteRequest (request: RequestHeader) = {
		val subdomain = getSubdomain(request)
		if (subdomain == "" || subdomain == "www" || subdomain == "localhost")
			web.Routes.routes.lift(request)
		else if (subdomain == "admin")
			admin.Routes.routes.lift(request)
		else
			None	//super.onRouteRequest(request)
	}
	
	
	// 404 - page not found error
	override def onHandlerNotFound (request: RequestHeader) =
		if (getSubdomain(request) == "admin")
			GlobalAdmin.onHandlerNotFound(request)
		else
			GlobalWeb.onHandlerNotFound(request)
	
	// 500 - internal server error
	override def onError (request: RequestHeader, throwable: Throwable) =
		if (getSubdomain(request) == "admin")
			GlobalAdmin.onError(request, throwable)
		else
			GlobalWeb.onError(request, throwable)
	
	// called when a route is found, but it was not possible to bind the request parameters
	override def onBadRequest (request: RequestHeader, error: String) =
		if (getSubdomain(request) == "admin")
			GlobalAdmin.onBadRequest(request, error)
		else
			GlobalWeb.onBadRequest(request, error)

}