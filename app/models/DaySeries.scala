package models

import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{StringType, StructField, StructType, LongType, DateType, TimestampType}
import org.apache.spark.sql.functions._

import org.apache.spark.sql.functions.{desc, asc}
// import com.github.nscala_time.time.Imports._

import DB.session
import DB.session.implicits._

case class CountrySum(country: String, recovered: Long, deaths: Long, confirmed: Long){
    def this(row: org.apache.spark.sql.Row) = {
        this(
            row.getAs[String]("Country/Region"),
            row.getAs[Long]("Recovered"),
            row.getAs[Long]("Deaths"),
            row.getAs[Long]("Confirmed")
        )
    }
}

case class StateSum(country: String, recovered: Long, deaths: Long, confirmed: Long){
    def this(row: org.apache.spark.sql.Row) = {
        this(
            row.getAs[String]("Country/Region"),
            row.getAs[Long]("Recovered"),
            row.getAs[Long]("Deaths"),
            row.getAs[Long]("Confirmed")
        )
    }
}
case class DaySeries(dataframe: org.apache.spark.sql.DataFrame){        
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

    def view_country(country_name: String): DaySeries = {
        var country = filter_by("Country/Region", country_name).dataframe
        country = dataframe.groupBy(dataframe("Observation Date"))
            .agg(
                first("Last Update").as("Last Update"), // May cause errors; we'll see
                sum("Confirmed").as("Confirmed"),
                sum("Deaths").as("Deaths"),
                sum("Recovered").as("Recovered")
                )
            .withColumn("SNo", 
                lit(-1L)
                )
            .withColumn("Province/State", 
                lit("ALL")
                )
            .withColumn("Country/Region", 
                lit(country_name)
                )
        country.show()

        return DaySeries(country)
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

    def country_sum(){
        val sums = dataframe.groupBy("Country/Region")
            .agg(
                sum("Recovered").as("Recovered"), 
                sum("Deaths").as("Deaths"), 
                sum("Confirmed").as("Confirmed")
            )

        return sums.map(row => {new CountrySum(row)}).collect()
    }

    // sum by group?
    def state_sums(){
        val sums = dataframe.groupBy("Country/Region", "Province/State")
            .agg(
                sum("Recovered").as("Recovered"), 
                sum("Deaths").as("Deaths"), 
                sum("Confirmed").as("Confirmed")
            )

        return sums.map(row => new StateSum(row)).collect()
    }
}