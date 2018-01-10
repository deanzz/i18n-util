package km.i18n.util.conf

import com.typesafe.config.Config

case class DBConf(uri: String, name: String)
object DBConf{
  def apply(params: Config): DBConf = {
    DBConf(
      params.getString("uri"),
      params.getString("name")
    )
  }
}