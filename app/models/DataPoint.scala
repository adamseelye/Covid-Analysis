package models

import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{StringType, StructField, StructType, LongType, DateType, TimestampType}
import org.apache.spark.sql.functions._

import org.apache.spark.sql.functions.{desc, asc}
import com.github.nscala_time.time.Imports._

import DB.session

object DataPoint {
    val session = DB.session
    val csv_path = "hdfs://localhost:9000/user/victorious/covid/datapoints"

    val schema = StructType(
        Array(
            StructField("ID", LongType, nullable=false),
            StructField("Observation Date", DateType, nullable=false),
            StructField("Province/State", StringType, nullable=true),
            StructField("Country/Region", StringType, nullable=false),
            StructField("Last Update", TimestampType, nullable=false),
            StructField("Confirmed", LongType, nullable=false),
            StructField("Deaths", LongType, nullable=false),
            StructField("Recovered", LongType, nullable=false)
        )
    )

    var dataframe = session
        .read.format("csv")
        .option("header", "true")
        .load(csv_path)


    dataframe.cache() // in case we do CRUD
    dataframe.creatOrReplaceTempView("datapoint")
    
    val timeseries = new TimeSeries(dataframe)

    // Default Datapoint is intentionally weird; effectively an exception,
    // handle it somehow.
    val empty = DataPoint()  

    // Called for debugging; if you really want some datapoints, use view
    // instead.
    def show() = {
        dataframe = session.sql("SELECT DISTINCT * FROM root ORDER BY root")
        dataframe.show()
    }

    def country(country: String = "*"){
        return timeseries.country(country, order)
    }

    def state(state: String = "*"){
        return timeseries.state(state, order)
    }
}


class TimeSeries(dataframe: org.apache.spark.sql.DataFrame){
    val dataframe = dataframe
    val datapoints = dataframe.map { row =>
        DataPoint(row)
    }.collect

    // filter
    def filter_by(colun_name: String, value: String = "*"){
        return new TimeSeries(dataframe.filter(dataframe(column_name) === value))
    }
    def country(country_name: String = "*"){
        return filter_by("Country/Region", country_name)
    }

    def state(state: String = "*"){
        return filter_by("Province/State", state)
    }

    // changing order
    def order_by(column_name: String, order: String = "ASC"){
        if(order == "ASC"){
            return new TimeSeries(dataframe.sort(asc(column_name)))
        }
        if(order == "DESC"){
            return new TimeSeries(dataframe.sort(desc(column_name)))
        }
    }

    def by_date(order: String = "ASC"){
        return order_by("Observation Date", order)
    }

    def by_last_update(order: String = "ASC"){
        return order_by("Last Update", order)
    }

    def by_confirmed(order: String = "ASC"){
        return order_by("Confirmed", order)
    }

    def by_deaths(order: String = "ASC"){
        return order_by("Deaths", order)
    }

    def by_recovered(order: String = "ASC"){
        return order_by("Recovered", order)
    }

    // sum by group?
    

}

case class DataPoint(id: Long = -1L, observationDate: DateTime = DateTime.now, 
    state: String = "", country: String = "", 
    updated: DateTime = DateTime.now(), confirmed: Long = -1L, 
    death: Long = -1L, recovered: Long = -1L) {

}