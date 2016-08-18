package services

import java.io.File
import java.nio.charset.Charset

import org.apache.commons.io.FileUtils

/**
  * Created by mahesh on 18/08/16.
  */
object Util {

  def writeCsvToDir[T](data: Array[Array[T]], separator: String, dir: String) = {
    if(data != null && data.length > 0) {
      val csvData = data.map(row => row.mkString(separator)).mkString("\n")

      val csvFile = new File(dir, System.currentTimeMillis()+"_" + Math.abs(csvData.hashCode)+".csv")
      FileUtils.writeStringToFile(csvFile, csvData, Charset.defaultCharset())
    }
  }
}



