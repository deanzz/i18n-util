package km.i18n.util.msg

import java.io.File

import km.i18n.util.FileUtils
import km.i18n.util.conf._

import scala.collection.mutable
import scala.io.Source
import scala.util.matching.Regex

class Finder(conf: I18nConf) {
  val unmatchedLines = StringBuilder.newBuilder
  val messagesLines = StringBuilder.newBuilder
  val respCodeLines = StringBuilder.newBuilder
  val msgCodeMap = mutable.HashMap.empty[String, Int]
  val nameCodeMap = mutable.HashMap.empty[String, Int]

  def find = {
    kmErrorCodeAnalysis(conf.kmdmErrorCodeConf)
    IntegrationModuleErrorCodeAnalysis(conf.integrationModuleErrorCodeConf)
    AuthorityErrorCodeAnalysis(conf.authorityErrorCodeConf)

    conf.srcDirs.foreach {
      dir =>
        scanningSrc(dir, conf.srcFileExtension, conf.keyword, conf.destDir)
    }

    msgCodeMap.toSeq.sortBy(_._2).foreach {
      case (msg, code) =>
        messagesLines.append(s"${getMsgKey(code)} = $msg").append("\n")
    }

    nameCodeMap.toSeq.sortBy(_._2).foreach {
      case (name, code) =>
        respCodeLines.append(s"val $name = $code").append("\n")
    }

    val destDir = conf.destDir
    // write RespCode
    outputLines(new File(s"$destDir/respCodeLines.txt"), respCodeLines)
    // write messages
    outputLines(new File(s"$destDir/messagesLines.txt"), messagesLines)
    outputLines(new File(s"$destDir/unmatchedLines.txt"), unmatchedLines)
  }

  private def scanningSrc(srcDir: String, fileExtension: Seq[String], keyword: KeywordConf, destDir: String): Unit = {
    val root = new File(srcDir)
    val fileList = FileUtils.listFiles(root).filter {
      f =>
        fileExtension.map {
          e =>
            f.getName.endsWith(e)
        }.reduce((b1, b2) => b1 || b2)
    }

    val pattern1 = keyword.regex1.r
    val pattern2 = keyword.regex2.r
    val pattern3 = keyword.regex3.r
    val pattern4 = keyword.regex4.r
    val pattern5 = keyword.regex5.r
    val pattern6 = keyword.regex6.r
    val pattern7 = keyword.regex7.r
    val pattern8 = keyword.regex8.r
    val pattern9 = keyword.regex9.r
    val pattern10 = keyword.regex10.r
    val pattern11 = keyword.regex11.r
    val pattern12 = keyword.regex12.r


    fileList.foreach {
      f =>
        println(s"${f.getPath}")
        val lines = Source.fromFile(f).getLines().toArray
        val linesWithIdx = lines.zipWithIndex
        val parentheses = if (f.getName.endsWith(".java")) "()" else ""
        val lineEnd = if (f.getName.endsWith(".java")) ";" else ""
        var packageLine: (Int, String) = (0, "")
        val packageLineKeyword = "package "
        val resultLines = (
          for {
            (line, idx) <- linesWithIdx
            if line.startsWith(packageLineKeyword) || (!line.trim.startsWith("//") && line.contains(keyword.key))
          } yield {
            val showLineIdx = idx + 1
            if(line.startsWith(packageLineKeyword))
              packageLine = (idx, line)

            val match1Res = matchRegex(pattern1, line) match {
              case Some((_, msg)) =>
                val (code, _) = addNewMsg(msg)
                val name = s"CODE_$code"
                addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\([-]?\d+\s*,""", s"new KMException(RespCode.$name$parentheses,")))
              case _ => None
            }

            val match2Res = matchRegex(pattern2, line) match {
              case Some((name, msg)) =>
                val (code, _) = addNewMsg(msg)
                addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\(KmdmErrorCode\..+\s*,""", s"new KMException(RespCode.$name$parentheses,")))
              case _ => None
            }

            val match3Res = matchRegex(pattern3, line) match {
              case Some((name, msg)) =>
                val (code, _) = addNewMsg(msg)
                addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\(KmdmErrorCode\..+\s*,""", s"new KMException(RespCode.$name$parentheses,")))
              case _ => None
            }

            val match4Res = matchRegex(pattern4, line) match {
              case Some((name, msg)) =>
                val (code, _) = addNewMsg(msg)
                addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\(KmdmErrorCode\..+\s*,""", s"new KMException(RespCode.$name$parentheses,")))
              case _ => None
            }

            val match5Res = matchRegex(pattern5, line) match {
              case Some((name, msg)) =>
                val (code, _) = addNewMsg(msg)
                addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\(KmdmErrorCode\..+\s*,""", s"new KMException(RespCode.$name$parentheses,")))
              case _ => None
            }

            val match6Res = matchRegex(pattern6, line) match {
              case Some((name, msg)) =>
                val (code, _) = addNewMsg(msg)
                addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\(AuthorityErrorCode\..+\s*,""", s"new KMException(RespCode.$name$parentheses,")))
              case _ => None
            }

            val match7Res = matchRegex(pattern7, line) match {
              case Some((name, _)) =>
                // Do not need add msg and name
                //addNewMsg(msg)
                //addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\(KmdmErrorCode\..+\s*,""", s"new KMException(RespCode.$name$parentheses,")))
              case _ => None
            }

            val match8Res = matchRegex(pattern8, line) match {
              case Some((name, _)) =>
                // Do not need add msg and name
                //addNewMsg(msg)
                //addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\(IntegrationModuleErrorCode\..+\s*,""", s"new KMException(RespCode.$name(),")))
              case _ => None
            }

            val match9Res = matchRegex(pattern9, line) match {
              case Some((name, _)) =>
                // Do not need add msg and name
                //addNewMsg(msg)
                //addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\(AuthorityErrorCode\..+\s*,""", s"new KMException(RespCode.$name$parentheses,")))
              case _ => None
            }

            val match10Res = matchRegex(pattern10, line) match {
              case Some((name, _)) =>
                // Do not need add msg and name
                //addNewMsg(msg)
                //addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\(KmdmErrorCode\..+\s*,""", s"new KMException(RespCode.$name$parentheses,")))
              case _ => None
            }

            val match11Res = matchRegex(pattern11, line) match {
              case Some((name, _)) =>
                // Do not need add msg and name
                //addNewMsg(msg)
                //addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\(IntegrationModuleErrorCode\..+\s*,""", s"new KMException(RespCode.$name$parentheses,")))
              case _ => None
            }

            val match12Res = matchRegex(pattern12, line) match {
              case Some((name, _)) =>
                // Do not need add msg and name
                //addNewMsg(msg)
                //addNewName(name, code)
                // get new line that replace code and messageArgs in KMException parameters
                Some((idx, line.replaceAll("""new KMException\(AuthorityErrorCode\..+\s*,""", s"new KMException(RespCode.$name$parentheses,")))
              case _ => None
            }

            if (match1Res.isEmpty &&
              match2Res.isEmpty &&
              match3Res.isEmpty &&
              match4Res.isEmpty &&
              match5Res.isEmpty &&
              match6Res.isEmpty &&
              match7Res.isEmpty &&
              match8Res.isEmpty &&
              match9Res.isEmpty &&
              match10Res.isEmpty &&
              match11Res.isEmpty &&
              match12Res.isEmpty && !line.startsWith(packageLineKeyword)) {
              unmatchedLines.append(s"${f.getName}#$showLineIdx#$line").append("\n")
              None
            } else {
              if (match1Res.isDefined) match1Res
              else if (match2Res.isDefined) match2Res
              else if (match3Res.isDefined) match3Res
              else if (match4Res.isDefined) match4Res
              else if (match5Res.isDefined) match5Res
              else if (match6Res.isDefined) match6Res
              else if (match7Res.isDefined) match7Res
              else if (match8Res.isDefined) match8Res
              else if (match9Res.isDefined) match9Res
              else if (match10Res.isDefined) match10Res
              else if (match11Res.isDefined) match11Res
              else match12Res
            }

          }).filter(_.nonEmpty)

        if (resultLines.nonEmpty) {

          lines.update(packageLine._1, s"${packageLine._2}\n\nimport elemental.i18n.RespCode$lineEnd")
          resultLines.foreach {
            case Some((idx, newLine)) =>
              lines.update(idx, newLine)
          }

          //write replaced src file
          outputLines(f, lines.mkString("\n"))
        }
    }
  }

  private def kmErrorCodeAnalysis(conf: KmdmErrorCodeConf) = {
    ErrorCodeAnalysis(conf.filePath, conf.msgRegex)
  }

  private def IntegrationModuleErrorCodeAnalysis(conf: IntegrationModuleErrorCodeConf) = {
    ErrorCodeAnalysis(conf.filePath, conf.msgRegex)
  }

  private def AuthorityErrorCodeAnalysis(conf: AuthorityErrorCodeConf) = {
    ErrorCodeAnalysis(conf.filePath, conf.msgRegex)
  }

  private def ErrorCodeAnalysis(filePath: String, msgRegex: String) = {
    val f = new File(filePath)
    val msgPattern = msgRegex.r
    val lines = Source.fromFile(f).getLines().toArray
    val res = lines.filter(!_.trim.startsWith("//")).map {
      line =>
        val matchOpt = matchRegex(msgPattern, line)
        matchOpt match {
          case Some((pName, msg)) =>
            Some((s"${pName}_CODE", msg))
          case _ =>
            matchRegex("""RESULT_NOT_PERCEPTION(\s*)= (.+);""".r, line) match {
              case Some((_, msg)) =>
                Some(("RESULT_NOT_PERCEPTION", msg))
              case _ =>
                unmatchedLines.append(line).append("\n")
                None
            }
        }
    }.filter(_.nonEmpty)

    res.foreach {
      case Some((name, msg)) =>
        val (code, _) = addNewMsg(msg)
        addNewName(name, code)
      //(code, msg, name)
    }
  }

  private def matchRegex(pattern: Regex, line: String) = {
    pattern.findAllIn(line).matchData.map {
      m =>
        val key = m.group(1).trim.replace(")", "")
        val value = m.group(2).trim.replace(")", "")
        (key, value)
    }.toSeq.headOption
  }

  /*  private def findCodeMsg(pattern: Regex, line: String) = {
      val infoOpt = matchRegex(pattern, line)
      infoOpt match {
        case Some((origCode, msg)) =>
          val (code, _) = addNewMsg(msg)
          Some((origCode, code, msg))
        case _ => None
      }
    }*/

  private def addNewMsg(msg: String) = {
    if (!msgCodeMap.contains(msg)) {
      val newErrCode = genErrorCode(msgCodeMap)
      msgCodeMap.put(msg, newErrCode)
      (newErrCode, msg)
    } else {
      (msgCodeMap(msg), msg)
    }
  }

  private def addNewName(name: String, code: Int) = {
    if (!nameCodeMap.contains(name)) {
      nameCodeMap.put(name, code)
      (code, name)
    } else {
      (nameCodeMap(name), name)
    }
  }

  private def genErrorCode(map: mutable.HashMap[String, Int]) = {
    if (map.isEmpty) conf.codeStartIdx
    else map.values.max + 1
  }

  private def getMsgKey(code: Int) = {
    s"r.$code"
  }

  private def outputLines(file: File, stringBuilder: StringBuilder) = {
    FileUtils.writeToFile(file, stringBuilder.toString())
  }

  private def outputLines(file: File, strs: String) = {
    FileUtils.writeToFile(file, strs)
  }

}
