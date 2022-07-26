package controllers

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

// Spark
import models.DaySeries

class Graph @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  // def state(country: String, state: String, stat: String) = Action { implicit request =>
  //   val dayseries = models.Day.dayseries
  //                     .country(country)
  //                     .state(state).by_date()
  //   Ok(views.html.graph.state(dayseries, state, stat))
  // }

  def overall(stat: String) = Action { implicit request =>
    val datapoints = models.Day.dayseries.view_overall().by_date().datapoints
    val dateseries = datapoints.map(day => day.date.toString)
    var statseries: Array[Long] = Array()
    if(stat == "Deaths"){
      statseries = datapoints.map(day => day.deaths)
    }else if(stat == "Recovered"){
      statseries = datapoints.map(day => day.recovered)
    }else if(stat == "Confirmed"){
      statseries = datapoints.map(day => day.confirmed)
    }

    Ok(views.html.graph.overall(dateseries, statseries, stat))
  }

  def country(country: String, stat: String) = Action { implicit request => 
    val datapoints = models.Day.dayseries.view_country(country).by_date().datapoints
    val dateseries = datapoints.map(day => day.date.toString)
    var statseries: Array[Long] = Array()

    if(stat == "Deaths"){
      statseries = datapoints.map(day => day.deaths)
    }else if(stat == "Recovered"){
      statseries = datapoints.map(day => day.recovered)
    }else if(stat == "Confirmed"){
      statseries = datapoints.map(day => day.confirmed)
    }

    Ok(views.html.graph.country(dateseries, statseries, country, stat))
  }
}
