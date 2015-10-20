package controllers.admin

import play.api.Configuration
import javax.inject.Inject

class Assets @Inject() (val errorHandler: admin.ErrorHandler) extends controllers.common.Assets(errorHandler)
class SharedResources @Inject() (val errorHandler: admin.ErrorHandler, val conf: Configuration) extends controllers.common.SharedResources(errorHandler, conf)