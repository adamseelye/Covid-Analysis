// Wraps Dataframe of State Aggregates/ Array of StateSums
package models

import org.apache.spark.sql.Row
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{StringType, StructField, StructType, LongType, DateType, TimestampType}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window

import org.apache.spark.sql.functions.{desc, asc}

case class StateSeries(dataframe: org.apache.spark.sql.DataFrame){
  val page_length = 20

  dataframe.show()

  def datapoints(): Array[StateSum] = {
    return dataframe.rdd.map( row => new StateSum(row)).collect
  }

  def length(): Int = {
    return dataframe.count.toInt
  }

  def pages(): Int = {
    return (length() / page_length).toInt
  }

  def order_by(column_name: String, order: String = "ASC"): StateSeries = {
    if(order == "ASC"){
      return new StateSeries(dataframe.sort(asc(column_name)))
    }else{
      return new StateSeries(dataframe.sort(desc(column_name)))
    }
  }

  def page(index: Int, size: Int = page_length): StateSeries = {
    val window  = Window.partitionBy(lit("a")).orderBy(lit("a"))
    val indexed = dataframe.withColumn(
      "row_num",
      row_number().over(window)
    )
        
    val start = index * size 
    val end = (index + 1) * size - 1
    println(start)
    println(end)
    
    return StateSeries(
        indexed.filter(col("row_num").between(start, end))
    )
    }
}