package km.i18n.util.conf

import com.typesafe.config.Config

case class IntegrationModuleErrorCodeConf(filePath: String, msgRegex: String)

object IntegrationModuleErrorCodeConf{
  def apply(params: Config): IntegrationModuleErrorCodeConf = {
    IntegrationModuleErrorCodeConf(
      params.getString("file-path"),
      params.getString("msg-regex")
    )
  }
}