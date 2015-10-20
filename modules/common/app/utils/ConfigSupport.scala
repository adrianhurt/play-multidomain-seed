package utils

import play.api.Configuration

trait ConfigSupport {
  def conf: Configuration

  def config(path: String): Option[Configuration] = conf.getConfig(path)
  def requiredConfig(path: String): Configuration = config(path).getOrElse(confException(path))

  def confGetString(path: String, config: Configuration = conf): Option[String] = config.getString(path)
  def confRequiredString(path: String, config: Configuration = conf): String = confGetString(path, config).getOrElse(confException(path))

  def confGetInt(path: String, config: Configuration = conf): Option[Int] = config.getInt(path)
  def confRequiredInt(path: String, config: Configuration = conf): Int = confGetInt(path, config).getOrElse(confException(path))

  def confGetBoolean(path: String, config: Configuration = conf): Option[Boolean] = config.getBoolean(path)
  def confRequiredBoolean(path: String, config: Configuration = conf): Boolean = confGetBoolean(path, config).getOrElse(confException(path))

  private def confException(path: String) = throw new IllegalArgumentException(s"Missing $path within configuration file.")
}
