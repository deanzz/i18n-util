package km.i18n.util

import java.io.{BufferedWriter, File, FileWriter}

object FileUtils {

  def listFiles(dir: File): Array[File] = {
    val these = dir.listFiles
    these ++ these.filter(_.isDirectory).flatMap(listFiles)
  }

  def writeToFile(file: File, content: String): Unit = {
    val bw = new BufferedWriter(new FileWriter(file))
    try {
      bw.write(content)
    } finally {
      bw.close()
    }
  }

}
