package km.i18n.util.module

import java.io.File

import km.i18n.util.FileUtils
import org.json4s.JsonAST._
import org.json4s.native.JsonParser

import scala.io.Source
import scala.util.Try

trait BaseProcessor {
  implicit val formats = org.json4s.DefaultFormats
  //val chineseRegex = """.*[\u4e00-\u9fa5]+.*"""

  protected def file2Map(f: File) = {
    Source.fromFile(f).getLines().map {
      s =>
        val arr = s.split(",")
        if (arr.length > 1) (arr.head, arr.last)
        else (arr.head, arr.head)
    }.toMap
  }

  protected def getAttributes(attributes: Seq[String]) = {
    attributes.flatMap {
      jsonStr =>
        getOneAttribute(jsonStr)
    }.distinct
  }

  protected def getOneAttribute(jsonStr: String) = {
    val json = JsonParser.parse(if (jsonStr.isEmpty) "[]" else jsonStr)
    val names: Seq[String] = extractStrings(json, "name")
    val titles: Seq[String] = extractStrings(json, "title")
    val descriptions: Seq[String] = extractStrings(json, "description")
    //
    val errMsgs: Seq[String] = extractStrings(json, "errMsg")
    val values: Seq[String] = extractStrings(json, "values")
    //
    //val tipsValues: Seq[String] = extractTipsValues(json)
    (names ++ titles ++ descriptions ++ errMsgs ++ values /* ++ tipsValues*/).distinct
  }

  protected def extractStrings(jValue: JValue, k: String): Seq[String] = {
    val seq: Seq[String] = for {
      JObject(list) <- jValue \\ k
      JField(k, JString(v)) <- list
    } yield v.replace("\n", "\\n")

    val seq1: Seq[String] = for {
      JArray(list) <- jValue \\ k
      JString(v) <- list
    } yield v.replace("\n", "\\n")

    val all = seq ++ seq1
    if (all.isEmpty) {
      Try(Seq((jValue \\ k).extract[String].replace("\n", "\\n"))).getOrElse(Seq.empty[String])
    } else all
  }

  protected def extractTipsValues(jValue: JValue): Seq[String] = {
    for {
      JObject(obj) <- jValue \\ "tips"
      JField("values", JString(v)) <- obj
    } yield v
  }

  protected def outputLines(file: File, seq: Seq[String]) = {
    FileUtils.writeToFile(file, seq.mkString("\n"))
  }

  protected def outputLines(file: File, stringBuilder: StringBuilder) = {
    FileUtils.writeToFile(file, stringBuilder.toString())
  }

  protected def outputLines(file: File, strs: String) = {
    FileUtils.writeToFile(file, strs)
  }

  protected def containsChinese(s: String): Boolean = {
    s.exists {
      c => Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN
    }
    //s.matches(chineseRegex)
  }
}
