package km.i18n.util.conf

import com.typesafe.config.Config


case class KmdmErrorCodeConf(filePath: String, msgRegex: String)

object KmdmErrorCodeConf{
  def apply(params: Config): KmdmErrorCodeConf = {
    KmdmErrorCodeConf(
      params.getString("file-path"),
      params.getString("msg-regex")
    )
  }
}