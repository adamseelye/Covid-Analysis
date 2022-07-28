// Wraps Dataframe of Country Aggregates/ Array of CountrySums
package models

import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{StringType, StructField, StructType, LongType, DateType, TimestampType}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window

import org.apache.spark.sql.functions.{desc, asc}

case class CountrySeries(dataframe: org.apache.spark.sql.DataFrame){
  val page_length = 20

  dataframe.show()

  def datapoints(): Array[CountrySum] = {
    return dataframe.rdd.map( row => new CountrySum(row)).collect
  }

  def length(): Int = {
    return dataframe.count.toInt
  }

  def pages(): Int = {
    return (length() / page_length).toInt
  }

  def order_by(column_name: String, order: String = "ASC"): CountrySeries = {
    if(order == "ASC"){
      return new CountrySeries(dataframe.sort(asc(column_name)))
    }else{
      return new CountrySeries(dataframe.sort(desc(column_name)))
    }
  }

  def page(index: Int, size: Int = page_length): CountrySeries = {
    val window  = Window.partitionBy(lit("a")).orderBy(lit("a"))
    val indexed = dataframe.withColumn(
      "row_num",
      row_number().over(window)
    )
        
        val start = index * size 
        val end = (index + 1) * size - 1
        println(start)
        println(end)
        return CountrySeries(
            indexed.filter(col("row_num").between(start, end))
        )
    }
}