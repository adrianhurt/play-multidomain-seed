import javax.inject.Inject
import play.api.http._
import play.api.mvc.{ RequestHeader, EssentialAction, Action, Results }
import play.api.routing.Router

class VirtualHostRequestHandler @Inject() (errorHandler: HttpErrorHandler,
  configuration: HttpConfiguration, filters: HttpFilters,
  webRouter: web.Routes, adminRouter: admin.Routes) extends DefaultHttpRequestHandler(
  webRouter, errorHandler, configuration, filters
) {

  override def routeRequest(request: RequestHeader) = getSubdomain(request) match {
    case "admin" => adminRouter.routes.lift(rewriteAssets("admin", request))
    case _ => webRouter.routes.lift(rewriteAssets("web", request))
  }

  /*
	* Gets the subdomain: "admin" o "www"
	*/
  private def getSubdomain(request: RequestHeader) = request.domain.replaceFirst("[\\.]?[^\\.]+[\\.][^\\.]+$", "")

  /*
	* Rewrite the Assets routes for the root project, accessing to the corresponding lib.
	*/
  private def rewriteAssets(subproject: String, request: RequestHeader): RequestHeader = {
    val pub = s"""/public/(.*)""".r
    val css = s"""/css/(.*)""".r
    val js = s"""/js/(.*)""".r
    val img = s"""/img/(.*)""".r
    request.path match {
      case pub(file) => request.copy(path = s"/lib/$subproject/$file")
      case css(file) => request.copy(path = s"/lib/$subproject/stylesheets/$file")
      case js(file) => request.copy(path = s"/lib/$subproject/javascripts/$file")
      case img(file) => request.copy(path = s"/lib/$subproject/images/$file")
      case _ => request
    }
  }
}