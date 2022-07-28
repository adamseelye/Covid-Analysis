package models

import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{StringType, StructField, StructType, LongType, DateType, TimestampType}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window

import org.apache.spark.sql.functions.{desc, asc}

import DB.session
import DB.session.implicits._

case class DaySeries(dataframe: org.apache.spark.sql.DataFrame){
    val page_length = 25

    dataframe.show()

    def datapoints(): Array[Day] = {
        return dataframe.rdd.map( row => new Day(row)).collect
    }

    def length(): Int = {
        return dataframe.count.toInt
    }

    def pages(): Int = {
        return (length() / page_length).toInt
    }

    // filter
    def filter_by(column_name: String, value: String = "*"): DaySeries = {
        return new DaySeries(dataframe.filter(dataframe(column_name) === value))
    }

    def country(country_name: String = "*"): DaySeries = {
        return filter_by("Country", country_name)
    }

    def view_country(country_name: String): DaySeries = {
        var country = filter_by("Country", country_name).dataframe
        country = country.groupBy(country("Date"))
          .agg(
            first("Update").as("Update"), // May cause errors; we'll see
            sum("Confirmed").as("Confirmed"),
            sum("Deaths").as("Deaths"),
            sum("Recovered").as("Recovered")
          )
          .withColumn("SNo", lit(-1L))
          .withColumn("State", lit("ALL"))
          .withColumn("Country", lit(country_name))
        
        country.show()

        return new DaySeries(country)
    }

    
    def view_overall(): DaySeries = {
      val overall = dataframe.groupBy(dataframe("Date"))
        .agg(
          first("Update").as("Update"), // May cause errors; we'll see
          sum("Confirmed").as("Confirmed"),
          sum("Deaths").as("Deaths"),
          sum("Recovered").as("Recovered")
        )
        .withColumn("SNo", lit(-1L))
        .withColumn("State", lit("ALL"))
        .withColumn("Country", lit("ALL"))

      overall.show()

      return new DaySeries(overall)
    }

    def state(state: String = "*"): DaySeries = {
        return filter_by("State", state)
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
        return order_by("Date", order)
    }

    def by_last_update(order: String = "ASC"): DaySeries = {
        return order_by("Update", order)
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

    def page(index: Int, size: Int = 20): DaySeries = {
        val window  = Window.partitionBy(lit("a")).orderBy(lit("a"))
        val indexed = dataframe.withColumn(
            "row_num",
            row_number().over(window)
            )
        
        val start = index * size 
        val end = (index + 1) * size - 1
        println(start)
        println(end)
        return DaySeries(
            indexed.filter(col("row_num").between(start, end))
        )
    }

    // TODO: Ideally, I'd have a CountrySums collection that wrapped the datafame
    // and returned that.
    def country_series(column: String = "Country", 
                      order: String = "ASC", 
                      limit: Int = -1): CountrySeries = {
      var sums = dataframe
                .groupBy("Country")
                .agg(
                  dataframe("Country"),
                  countDistinct("State").as("States"),
                  max("Recovered").as("Recovered"), 
                  max("Deaths").as("Deaths"), 
                  max("Confirmed").as("Confirmed")
                )

      if(order == "ASC"){sums = sums.sort(asc(column))}
      else{sums = sums.sort(desc(column))}

      if(limit != -1){sums = sums.limit(limit)}

      return new CountrySeries(sums)
    }

    // sum by group?
    def state_series(country: String = "ALL", 
                    column: String = "State", 
                    order: String = "ASC", 
                    limit: Int = -1): StateSeries = {
      var sums = dataframe
                .groupBy("Country", "State")
                .agg(
                  max("Recovered").as("Recovered"), 
                  max("Deaths").as("Deaths"), 
                  max("Confirmed").as("Confirmed")
                )

      if(country != "ALL"){
        sums = sums.filter(sums("Country") === country)
      }

      if(order == "ASC"){sums = sums.sort(asc(column))}
      else{sums = sums.sort(desc(column))}

      if(limit != -1){sums = sums.limit(limit)}

      return StateSeries(sums)
    }
}