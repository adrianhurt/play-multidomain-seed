package controllers.common

import controllers.AssetsBuilder
import play.api.http.HttpErrorHandler
import controllers.Assets.Asset
import play.api.mvc.{Controller, Action}
import java.io.File


class Assets(errorHandler: HttpErrorHandler) extends AssetsBuilder(errorHandler) {
	def public (path: String, file: Asset) = versioned(path, file)
	def lib (path: String, file: Asset) = versioned(path, file)
	def css (path: String, file: Asset) = versioned(path, file)
	def commonCss (path: String, file: Asset) = versioned(path, file)
	def js (path: String, file: Asset) = versioned(path, file)
	def commonJs (path: String, file: Asset) = versioned(path, file)
	def img (path: String, file: Asset) = versioned(path, file)
	def commonImg (path: String, file: Asset) = versioned(path, file)
}

/*
* Shared resources between subprojects. The base path is defined by the "rsc.folder" in the conf file.
*/
class SharedResources extends Controller {
	private lazy val rscFolder = play.api.Play.current.configuration.getString("rsc.folder").get
	private def rscPath (uri: String) = rscFolder + uri
	
	def rsc (file: String) = Action(Ok.sendFile(new File(rscPath(file))))
}
