package models

import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{StringType, StructField, StructType, LongType, DateType, TimestampType}
import org.apache.spark.sql.functions._

import org.apache.spark.sql.functions.{desc, asc}
// import com.github.nscala_time.time.Imports._

import DB.session

case class Day(id: String = "", observationDate: String = "", 
  state: String = "", country: String = "", updated: String = "", 
  confirmed: String = "", deaths: String = "", recovered: String = "") {
  
  def this(row: org.apache.spark.sql.Row) = {
    this(
      row.getAs[String]("SNo"),
      row.getAs[String]("Observation Date"),
      row.getAs[String]("Province/State"),
      row.getAs[String]("Country/Region"),
      row.getAs[String]("Last Update"),
      row.getAs[String]("Confirmed"),
      row.getAs[String]("Deaths"),
      row.getAs[String]("Recovered")
    )
  }
}

object Day {
    val session = DB.session
    val csv_path = "hdfs://localhost:9000/user/victorious/datapoints/covid_19_data.csv"

    val schema = StructType(
        Array(
            StructField("SNo", StringType, nullable=false),
            StructField("Observation Date", StringType, nullable=false),
            StructField("Province/State", StringType, nullable=true),
            StructField("Country/Region", StringType, nullable=false),
            StructField("Last Update", StringType, nullable=false),
            StructField("Confirmed", StringType, nullable=false),
            StructField("Deaths", StringType, nullable=false),
            StructField("Recovered", StringType, nullable=false)
        )
    )

    var dataframe = session
        .read.format("csv")
        .option("header", "true")
        .load(csv_path)


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

    def country(country: String = "*"){
        return dayseries.country(country)
    }

    def state(state: String = "*"){
        return dayseries.state(state)
    }
}

class DaySeries(dataframe: org.apache.spark.sql.DataFrame){
    val datapoints = dataframe.rdd.map( row =>
        new Day(row)
    ).collect

    // filter
    def filter_by(column_name: String, value: String = "*"): DaySeries = {
        return new DaySeries(dataframe.filter(dataframe(column_name) === value))
    }
    def country(country_name: String = "*"): DaySeries = {
        return filter_by("Country/Region", country_name)
    }

    def state(state: String = "*"): DaySeries = {
        return filter_by("Province/State", state)
    }

    // changing order
    def order_by(column_name: String, order: String = "ASC"): DaySeries = {
        if(order == "ASC"){
            return new DaySeries(dataframe.sort(asc(column_name)))
        }else{
            return new DaySeries(dataframe.sort(desc(column_name)))
        }
    }

    def by_date(order: String = "ASC"): DaySeries = {
        return order_by("Observation Date", order)
    }

    def by_last_update(order: String = "ASC"): DaySeries = {
        return order_by("Last Update", order)
    }

    def by_confirmed(order: String = "ASC"): DaySeries = {
        return order_by("Confirmed", order)
    }

    def by_deaths(order: String = "ASC"): DaySeries = {
        return order_by("Deaths", order)
    }

    def by_recovered(order: String = "ASC"): DaySeries = {
        return order_by("Recovered", order)
    }

    // sum by group?


}