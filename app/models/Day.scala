package models

import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{StringType, StructField, StructType, LongType, DateType, TimestampType}
import org.apache.spark.sql.functions._

import org.apache.spark.sql.functions.{desc, asc}
// import com.github.nscala_time.time.Imports._

import com.github.nscala_time.time.Imports._

import DB.session

case class Day(id:Long = -1L, 
  date: java.sql.Date = new java.sql.Date(1970, 1, 1), state: String = "", 
  country: String = "", 
  updated: java.sql.Timestamp = new java.sql.Timestamp(0L), 
  confirmed: Long = -1L, deaths: Long = -1L, recovered: Long = -1L) {
  
  def this(row: org.apache.spark.sql.Row) = {
    this(
      row.getAs[Long]("SNo"),
      row.getAs[java.sql.Date]("Observation Date"),
      row.getAs[String]("Province/State"),
      row.getAs[String]("Country/Region"),
      row.getAs[java.sql.Timestamp]("Last Update"),
      row.getAs[Long]("Confirmed"),
      row.getAs[Long]("Deaths"),
      row.getAs[Long]("Recovered")
    )
  }
}

object Day {
    val session = DB.session
    //val csv_path = "hdfs://localhost:9000/user/victorious/datapoints/covid_19_data.csv"
    val csv_path = "https://s3.us-east-2.amazonaws.com/covidanalysis/Data_setP2/covid_19_data.csv"

    val schema = StructType(
        Array(
            StructField("SNo", LongType, nullable=false),
            StructField("Observation Date", StringType, nullable=false),
            StructField("Province/State", StringType, nullable=true),
            StructField("Country/Region", StringType, nullable=false),
            StructField("Last Update", StringType, nullable=false),
            StructField("Confirmed", LongType, nullable=false),
            StructField("Deaths", LongType, nullable=false),
            StructField("Recovered", LongType, nullable=false)
        )
    )

    // TODO:  Pass in the schema when getting from csv
    var dataframe = session
        .read.schema(schema).format("csv")
        .option("header", "true")
        .load(csv_path)
    
    // Not sure how else to parse the date and timestamp
    dataframe = dataframe.select(
        dataframe("SNo"),
        to_date(dataframe("Observation Date"), "MM/dd/yyyy").as("Observation Date"),
        dataframe("Province/State"),
        dataframe("Country/Region"),
        to_timestamp(dataframe("Last Update"), "MM/dd/yyyy HH:mm").as("Last Update"),
        dataframe("Confirmed"),
        dataframe("Deaths"),
        dataframe("Recovered")
    )


    dataframe.cache() // in case we do CRUD
    dataframe.createOrReplaceTempView("datapoint")
    
    val dayseries = new DaySeries(dataframe)

    // Default Datapoint is intentionally weird; effectively an exception,
    // handle it somehow.
    val empty = new Day()  

    // Called for debugging; if you really want some datapoints, use view
    // instead.
    def show() = {
        dataframe = session.sql("SELECT DISTINCT * FROM root ORDER BY root")
        dataframe.show()
    }

    def country(country: String = "*"): models.DaySeries = {
        return dayseries.country(country)
    }

    def state(state: String = "*"): models.DaySeries = {
        return dayseries.state(state)
    }

    def country_sums(): Array[models.CountrySum] = {
        return dayseries.country_sums()
    }
}