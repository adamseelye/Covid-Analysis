package models

import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{StringType, StructField, StructType, LongType, DateType, TimestampType}
import org.apache.spark.sql.functions._

import org.apache.spark.sql.functions.{desc, asc}

import DB.session
import DB.session.implicits._

case class CountrySum(country: String, states: Long, recovered: Long, 
                      deaths: Long, confirmed: Long){
  def this(row: org.apache.spark.sql.Row) = {
    this(
      row.getAs[String]("Country"),
      row.getAs[Long]("States"),
      row.getAs[Long]("Recovered"),
      row.getAs[Long]("Deaths"),
      row.getAs[Long]("Confirmed")
    )
  }
}