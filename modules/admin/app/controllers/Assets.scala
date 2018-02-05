package controllers.admin

import play.api.Configuration
import javax.inject.Inject

import controllers.AssetsMetadata

class Assets @Inject() (errorHandler: admin.ErrorHandler, assetsMetadata: AssetsMetadata) extends controllers.common.Assets(errorHandler, assetsMetadata)
class SharedResources @Inject() (errorHandler: admin.ErrorHandler, conf: Configuration) extends controllers.common.SharedResources(errorHandler, conf)