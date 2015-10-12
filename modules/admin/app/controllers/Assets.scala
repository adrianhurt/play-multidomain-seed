package controllers.admin

class Assets @javax.inject.Inject() (val errorHandler: admin.ErrorHandler) extends controllers.common.Assets(errorHandler)
class SharedResources extends controllers.common.SharedResources