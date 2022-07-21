object databricks_template {
  def option1(): Unit = {
    import org.apache.commons.io.IOUtils
    import java.net.URL
    // bucket URL
    val urlfile=new URL("https://s3.us-east-2.amazonaws.com/covidanalysis/Data_setP2/covid_19_data.csv")
    val testcsvgit = IOUtils.toString(urlfile,"UTF-8").lines.toList.toDS()
    val testcsv = spark
      .read.option("header", true)
      .option("inferSchema", true)
      .csv(testcsvgit)
    testcsv.show

  }

  def option2(): Unit = {
    import java.net.URL
    import org.apache.spark.SparkFiles
    // bucket URL
    val urlfile="https://s3.us-east-2.amazonaws.com/covidanalysis/Data_setP2/covid_19_data.csv"
    spark.sparkContext.addFile(urlfile)

    val df = spark.read
      .option("inferSchema", true)
      .option("header", true)
      .csv("file://"+SparkFiles.get("covid_19_data.csv"))
    df.show

  }

}