package km.i18n.util

import java.util.Locale

import elemental.i18n.RespCode
import km.i18n.util.conf.ConfigLoader
import km.i18n.util.module.{ProcessorV1, ProcessorV2}
import km.i18n.util.msg.Finder
import elemental.i18n.impl.ScalaI18nResp
import elemental.i18n.modules.impl.JavaModulesI18nUtil
import elemental.i18n.util.{Lang, Messages}
import km.i18n.util.test.ErrorCode
import collection.JavaConverters._

object Main {

  def main(args: Array[String]): Unit = {
    val path = "/Users/deanzhang/work/code/github/i18n-util/conf/application.conf"
    val conf = ConfigLoader.load(path)
    //val finder = new Finder(conf)
    //finder.find
    //val moduleProcessor = new ProcessorV1(conf)
    //moduleProcessor.export
    //moduleProcessor.importI18n

    //val moduleProcessorV2 = new ProcessorV2(conf)
    //moduleProcessorV2.extractAll()
    //moduleProcessorV2.importAttributeTpl()
    i18nTest
  }

  def i18nTest = {
    //Messages.findBundleFiles(baseName, Lang(new Locale("zh", "CN")))
    val resp = new ScalaI18nResp()
    implicit val lang = Lang(new Locale("zh", "CN"))
    val msg = resp.message(ErrorCode.CODE_888)
    println(s"msg = $msg")
    val msg1 = resp.message(ErrorCode.DOWNLOAD_FILE_UNREADY_CODE)
    println(s"msg1 = $msg1")

    val moduleUtil = new JavaModulesI18nUtil
    val name1 = moduleUtil.javaName("IG_SECOND", lang)
    println(s"name1 = $name1")
    val name2 = moduleUtil.javaName("DEAN_ALGO", lang)
    println(s"name2 = $name2")

    val attribute1 = moduleUtil.javaAttribute("IG_SECOND", "KEY_1", lang)
    println(s"attribute1 = $attribute1")
    val attribute2 = moduleUtil.javaAttribute("K_MEANS", "KEY_50", lang)
    println(s"attribute2 = $attribute2")

    val attributes = moduleUtil.javaAttributes("K_MEANS", lang).asScala
    attributes.foreach{
      case (k, v) =>
        println(s"k = $k, v = $v")
    }
  }
}
