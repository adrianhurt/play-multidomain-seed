package controllers.web

import play.api.Configuration
import javax.inject.Inject

class Assets @Inject() (errorHandler: web.ErrorHandler) extends controllers.common.Assets(errorHandler)
class SharedResources @Inject() (errorHandler: web.ErrorHandler, conf: Configuration) extends controllers.common.SharedResources(errorHandler, conf)