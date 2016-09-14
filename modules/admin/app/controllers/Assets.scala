package controllers.admin

import play.api.Configuration
import javax.inject.Inject

class Assets @Inject() (errorHandler: admin.ErrorHandler) extends controllers.common.Assets(errorHandler)
class SharedResources @Inject() (errorHandler: admin.ErrorHandler, conf: Configuration) extends controllers.common.SharedResources(errorHandler, conf)