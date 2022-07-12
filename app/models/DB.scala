package models

import org.apache.spark.sql.SparkSession
import java.util.Properties

import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.Row
import org.apache.spark.SparkConf

// import spark.implicits._

object DB {
    System.setProperty("hadoop.home.dir", "~/hadoop/hadoop-3.3.3")

    val session = SparkSession
        .builder
        .appName("Covid-Project")
        .config("spark.master", "local[*]")
        .config("spark.sql.crossJoin.enabled" , "true" )
        .enableHiveSupport()
        .getOrCreate() 

    val url = "jdbc:mysql://localhost:3306/covide"
    val user = "roman"
    val password = ""
}
