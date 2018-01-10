package km.i18n.util.conf

import com.typesafe.config.Config

case class KeywordConf(key: String, regex1: String, regex2: String, regex3: String,
                       regex4: String, regex5: String, regex6: String, regex7: String,
                       regex8: String, regex9: String, regex10: String, regex11: String, regex12: String)

object KeywordConf{
  def apply(params: Config): KeywordConf = {
    KeywordConf(
      params.getString("key"),
      params.getString("regex1"),
      params.getString("regex2"),
      params.getString("regex3"),
      params.getString("regex4"),
      params.getString("regex5"),
      params.getString("regex6"),
      params.getString("regex7"),
      params.getString("regex8"),
      params.getString("regex9"),
      params.getString("regex10"),
      params.getString("regex11"),
      params.getString("regex12")
    )
  }
}