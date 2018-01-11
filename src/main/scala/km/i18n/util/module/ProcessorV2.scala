package km.i18n.util.module

import java.io.File

import km.i18n.util.conf.I18nConf
import km.i18n.util.module.dao.{ModuleInfo, ModuleInfoDao}

import scala.concurrent.Await
import scala.util.{Failure, Success}
import scala.concurrent.duration.DurationInt
import scala.io.Source

class ProcessorV2(conf: I18nConf) extends BaseProcessor {
  val moduleInfoDao = new ModuleInfoDao(conf.dbConf.uri, conf.dbConf.name)
  val namesBuilder = StringBuilder.newBuilder
  val parentTypeBuilder = StringBuilder.newBuilder
  val attributeBuilder = StringBuilder.newBuilder
  val descriptionBuilder = StringBuilder.newBuilder
  val nodeTypeAttributeMap = collection.mutable.Map.empty[String, String]
  val nodeTypeAttributeTplBuilder = StringBuilder.newBuilder
  val destDir = conf.destDir


  def fieldExtract(filedName: String, allModules: Seq[ModuleInfo]): Unit = {
    allModules.foreach {
      m =>
        filedName match {
          case "name" =>
            val line = s"${m.nodeType} = ${m.name}"
            namesBuilder.append(line).append("\n")
          case "description" =>
            val line = s"${m.nodeType} = ${m.description.replace("\n", "\\n")}"
            descriptionBuilder.append(line).append("\n")
          case "parentType" =>
            val line = s"${m.nodeType} = ${m.parentType}"
            parentTypeBuilder.append(line).append("\n")
          case "attribute" =>
            var attributeStr = m.attribute
            val attributes = getOneAttribute(attributeStr).filter(containsChinese)
            val attributesWithIdx = attributes.zipWithIndex
            attributesWithIdx.foreach {
              case (s, idx) =>
                val attriKey = s"KEY$idx"
                val key = s"${m.nodeType}_$attriKey"
                attributeBuilder.append(s"$key = $s").append("\n")
                attributeStr = attributeStr.replaceFirst(s, attriKey)
            }
            nodeTypeAttributeMap += (m.nodeType -> attributeStr)
            nodeTypeAttributeTplBuilder.append(s"${m.nodeType} = attributeStr").append("\n")

          case _ => throw new UnsupportedOperationException
        }
    }
  }

  def fieldExtractAll(allModules: Seq[ModuleInfo]): Unit = {
    allModules.foreach {
      m =>
        val nameLine = s"${m.nodeType} = ${m.name}"
        namesBuilder.append(nameLine).append("\n")

        val descriptionLine = s"${m.nodeType} = ${m.description.replace("\n", "\\n")}"
        descriptionBuilder.append(descriptionLine).append("\n")

        val parentTypeLine = s"${m.nodeType} = ${m.parentType}"
        parentTypeBuilder.append(parentTypeLine).append("\n")

        var attributeStr = m.attribute
        val attributes = getOneAttribute(attributeStr).distinct.filter(containsChinese)
        val attributesWithIdx = attributes.zipWithIndex
        attributesWithIdx.filter(_._1.nonEmpty).foreach {
          case (s, idx) =>
            val attriKey = s"KEY_$idx"
            val key = s"${m.nodeType}_$attriKey"
            attributeBuilder.append(s"$key = $s").append("\n")
            //attributeStr = attributeStr.replaceFirst(s.replace("+","\\+"), attriKey)
            val target = "\"" + s.replace("\\n","\\\\n").replace("+","\\+").replace("(","\\(").replace(")","\\)").
              replace("*","\\*").replace("[", "\\[").replace("]", "\\]").replace(".", "\\.")/*.replace("-", "\\-")*/ + "\""
            attributeStr = attributeStr.replaceAll(target, "\"" + key + "\"")
        }
        nodeTypeAttributeMap += (m.nodeType -> attributeStr)
        nodeTypeAttributeTplBuilder.append(s"${m.nodeType} = $attributeStr").append("\n")
    }
  }

  def extractAll(): Unit = {
    val r = Await.result(moduleInfoDao.getAll, 30.seconds)
    fieldExtractAll(r)
    outputLines(new File(s"$destDir/modules_name_zh_CN.txt"), namesBuilder)
    outputLines(new File(s"$destDir/modules_parentType_zh_CN.txt"), parentTypeBuilder)
    outputLines(new File(s"$destDir/modules_description_zh_CN.txt"), descriptionBuilder)
    outputLines(new File(s"$destDir/modules_attribute_zh_CN.txt"), attributeBuilder)
    outputLines(new File(s"$destDir/modules_attribute_tpl.txt"), nodeTypeAttributeTplBuilder)
  }

  def importAttributeTpl(): Unit = {
    Source.fromFile(new File(s"$destDir/modules_attribute_tpl.txt")).getLines().foreach{
      s =>
        if(!s.endsWith(" = ")) {
          val Array(k, v, _*) = s.split(" = ")
          println(s"k = $k, v = $v")
          Await.result(moduleInfoDao.update(k, v), 30.seconds)
        }
    }
  }
}
