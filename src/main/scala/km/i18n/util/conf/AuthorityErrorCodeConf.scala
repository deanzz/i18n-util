package km.i18n.util.conf

import com.typesafe.config.Config


case class AuthorityErrorCodeConf(filePath: String, msgRegex: String)

object AuthorityErrorCodeConf{
  def apply(params: Config): AuthorityErrorCodeConf = {
    AuthorityErrorCodeConf(
      params.getString("file-path"),
      params.getString("msg-regex")
    )
  }
}
