import javax.inject.Inject

import play.api.http._
import play.api.mvc.request.RequestTarget
import play.api.mvc.{ Action, EssentialAction, RequestHeader, Results }
import play.api.routing.Router

class VirtualHostRequestHandler @Inject() (
    errorHandler: HttpErrorHandler,
    configuration: HttpConfiguration,
    filters: HttpFilters,
    webRouter: web.Routes,
    adminRouter: admin.Routes
) extends DefaultHttpRequestHandler(
  webRouter, errorHandler, configuration, filters
) {

  /*
	* Gets the subdomain: "admin" o "www"
	*/
  private def getSubdomain(request: RequestHeader) = request.domain.replaceFirst("[\\.]?[^\\.]+[\\.][^\\.]+$", "")

  /*
  * Delegates to each router depending on the corresponding subdomain
  */
  override def routeRequest(request: RequestHeader) = getSubdomain(request) match {
    case "admin" => adminRouter.routes.lift(rewriteAssets("admin", request))
    case _       => webRouter.routes.lift(rewriteAssets("web", request))
  }

  /*
	* Rewrite the Assets routes for the root project, accessing to the corresponding lib.
	*/
  private def rewriteAssets(subproject: String, request: RequestHeader): RequestHeader = {
    val pub = s"""/public/(.*)""".r
    val css = s"""/css/(.*)""".r
    val js = s"""/js/(.*)""".r
    val img = s"""/img/(.*)""".r
    request.path match {
      case pub(file) => request.withTarget(request.target.withPath(s"/lib/$subproject/$file"))
      case css(file) => request.withTarget(request.target.withPath(s"/lib/$subproject/stylesheets/$file"))
      case js(file)  => request.withTarget(request.target.withPath(s"/lib/$subproject/javascripts/$file"))
      case img(file) => request.withTarget(request.target.withPath(s"/lib/$subproject/images/$file"))
      case _         => request
    }
  }
}