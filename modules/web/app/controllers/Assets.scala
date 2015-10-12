package controllers.web

class Assets @javax.inject.Inject() (val errorHandler: web.ErrorHandler) extends controllers.common.Assets(errorHandler)
class SharedResources extends controllers.common.SharedResources