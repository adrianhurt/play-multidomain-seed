package controllers.web

import play.api.Configuration
import javax.inject.Inject

import controllers.AssetsMetadata

class Assets @Inject() (errorHandler: web.ErrorHandler, assetsMetadata: AssetsMetadata) extends controllers.common.Assets(errorHandler, assetsMetadata)
class SharedResources @Inject() (errorHandler: web.ErrorHandler, conf: Configuration) extends controllers.common.SharedResources(errorHandler, conf)