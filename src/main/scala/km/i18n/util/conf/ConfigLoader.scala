package km.i18n.util.conf

import java.io.File
import com.typesafe.config.{Config, ConfigFactory}
import scala.collection.JavaConverters._

object ConfigLoader {

  def load(path: String): I18nConf = {
    val file = new File(path)
    load(ConfigFactory.parseFile(file))
  }

  private def load(conf: Config): I18nConf = {
    val srcDirs = conf.getStringList("source.dirs").asScala
    val srcFileExtension = conf.getStringList("source.file-extension").asScala
    val keyword = KeywordConf(conf.getConfig("source.keyword"))
    val destDir = conf.getString("destination.dir")
    val integrationModuleErrorCodeConf = IntegrationModuleErrorCodeConf(conf.getConfig("source.IntegrationModuleErrorCode"))
    val kmdmErrorCodeConf = KmdmErrorCodeConf(conf.getConfig("source.KmdmErrorCode"))
    val dbConf = DBConf(conf.getConfig("mongodb"))
    val authorityErrorCodeConf = AuthorityErrorCodeConf(conf.getConfig("source.AuthorityErrorCode"))
    val codeStartIdx = conf.getInt("source.code-start-idx")
    I18nConf(srcDirs, srcFileExtension, keyword, destDir, kmdmErrorCodeConf, integrationModuleErrorCodeConf, authorityErrorCodeConf, dbConf, codeStartIdx)
  }
}
