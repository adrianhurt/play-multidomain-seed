package controllers.web

import play.api.Configuration
import javax.inject.Inject

class Assets @Inject() (val errorHandler: web.ErrorHandler) extends controllers.common.Assets(errorHandler)
class SharedResources @Inject() (val errorHandler: web.ErrorHandler, val conf: Configuration) extends controllers.common.SharedResources(errorHandler, conf)