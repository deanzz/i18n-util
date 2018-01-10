package km.i18n.util.module

import java.io.File

import km.i18n.util.FileUtils
import km.i18n.util.conf.I18nConf
import km.i18n.util.module.dao.ModuleInfoDao
import org.json4s.JsonAST.{JField, JObject, JString, JValue}
import org.json4s.native.JsonParser

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.io.Source

class ProcessorV1(conf: I18nConf) extends BaseProcessor {

  val moduleInfoDao = new ModuleInfoDao(conf.dbConf.uri, conf.dbConf.name)
  val namesBuilder = StringBuilder.newBuilder
  val parentTypeBuilder = StringBuilder.newBuilder
  val attributeBuilder = StringBuilder.newBuilder
  val descriptionBuilder = StringBuilder.newBuilder
  val destDir = conf.destDir

  def export = {
    val future = moduleInfoDao.getAll
    val res = Await.result(future, 30.seconds)
    val names = res.map(_.name).distinct.filter(_.nonEmpty).map(_ + ",")
    val parentTypes = res.map(_.parentType).distinct.filter(_.nonEmpty).map(_ + ",")
    val attributes = getAttributes(res.map(_.attribute)).filter(_.nonEmpty).map(_ + ",")
    val descriptions = res.map(_.description).distinct.filter(_.nonEmpty).map(_ + ",")

    outputLines(new File(s"$destDir/moduleInfo.name.txt"), names)
    outputLines(new File(s"$destDir/moduleInfo.parentType.txt"), parentTypes)
    outputLines(new File(s"$destDir/moduleInfo.attribute.txt"), attributes)
    outputLines(new File(s"$destDir/moduleInfo.description.txt"), descriptions)
  }

  def importI18n = {
    val future = moduleInfoDao.getAll
    val res = Await.result(future, 30.seconds)
    val nameMap = file2Map(new File(s"$destDir/moduleInfo.name.txt"))
    val parentTypeMap = file2Map(new File(s"$destDir/moduleInfo.parentType.txt"))
    val attributeMap = file2Map(new File(s"$destDir/moduleInfo.attribute.txt"))
    val descriptionMap = file2Map(new File(s"$destDir/moduleInfo.description.txt"))
    res.foreach{
      m =>
        val nameEn = nameMap.getOrElse(m.name, m.name)
        val parentTypeEn = parentTypeMap.getOrElse(m.parentType, m.parentType)
        val descriptionEn = descriptionMap.getOrElse(m.description, m.description)
        val attributeKeywords = getOneAttribute(m.attribute)
        var attributeEn = m.attribute
        attributeKeywords.foreach{
          k =>
            attributeEn = attributeEn.replace(k, attributeMap.getOrElse(k, k))
        }
        val f = moduleInfoDao.update(m._id, s"en-US $nameEn",  s"en-US $parentTypeEn", attributeEn, s"en-US $descriptionEn")
        Await.result(f, 30.seconds)
    }
  }

}
