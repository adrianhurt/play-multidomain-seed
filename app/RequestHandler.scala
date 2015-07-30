import javax.inject.Inject
import play.api.http._
import play.api.mvc.{RequestHeader, EssentialAction, Action, Results}
import play.api.routing.Router

class VirtualHostRequestHandler @Inject() (errorHandler: HttpErrorHandler,
    configuration: HttpConfiguration, filters: HttpFilters,
    webRouter: web.Routes, adminRouter: admin.Routes
  ) extends DefaultHttpRequestHandler(
    webRouter, errorHandler, configuration, filters
  ) {

	override def routeRequest (request: RequestHeader) = {
		getSubdomain(request) match {
      case "admin" => tryRewrite("admin", request, adminRouter)
      case _ => tryRewrite("web", request, webRouter)
    }
  }
	
	/*
	* Gets the subdomain: "admin" o "www"
	*/
	private def getSubdomain (request: RequestHeader) = request.domain.replaceFirst("[\\.]?[^\\.]+[\\.][^\\.]+$", "")
	
	/*
	* Tries one of the following url rewrites:
	* /css/ *file -> /public/lib/subproject/stylesheets/ *file
	* /js/ *file -> /public/lib/subproject/javascripts/ *file
	* /img/ *file -> /public/lib/subproject/images/ *file
	*/
	private def tryRewrite (subproject: String, request: RequestHeader, router: Router) = {
		val css = s"""/*css/(.*)""".r
		val js = s"""/*js/(.*)""".r
		val img = s"""/*img/(.*)""".r
		request.path match {
			case css(file) => redirect(s"/lib/$subproject/stylesheets/$file")
			case js(file) => redirect(s"/lib/$subproject/javascripts/$file")
			case img(file) => redirect(s"/lib/$subproject/images/$file")
			case _ => router.routes.lift(request)
		}
	}
	/*
	* Gets an Option[Handler] from a redirect
	*/
	private def redirect (path: String) = Some(
		EssentialAction { rh =>
			Action { rh: RequestHeader =>
				Results.Redirect(path)
			}.apply(rh)
		}
	)
}