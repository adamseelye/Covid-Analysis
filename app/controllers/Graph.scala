package controllers

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

// Spark
import models.DaySeries

class Graph @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
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

  
  def state(country: String, state: String, stat: String) = Action { implicit request =>
    val datapoints = models.Day.dayseries
                      .country(country)
                      .state(state).by_date().datapoints
    val dateseries = datapoints.map(day => day.date.toString)
    var statseries: Array[Long] = Array()

    if(stat == "Deaths"){
      statseries = datapoints.map(day => day.deaths)
    }else if(stat == "Recovered"){
      statseries = datapoints.map(day => day.recovered)
    }else if(stat == "Confirmed"){
      statseries = datapoints.map(day => day.confirmed)
    }

    Ok(views.html.graph.state(dateseries, statseries, country, state, stat))
  }

  def countries(stat: String) = Action { implicit request =>
    val countrysums = models
                      .Day
                      .dayseries
                      .country_series(stat, "DESC", 25)
                      .datapoints
    val countryseries = countrysums.map( cs => cs.country )
    var statseries: Array[Long] = Array()

    if(stat == "Deaths"){statseries = countrysums.map(cs => cs.deaths)}
    else if(stat == "Recovered"){statseries = countrysums.map(cs => cs.recovered)}
    else if(stat == "Confirmed"){statseries = countrysums.map(cs => cs.confirmed)}

    Ok(views.html.graph.country_view(countryseries, statseries, stat))
  }


  def states(country: String = "ALL", stat: String) = Action { implicit request =>
    val statesums = models
                    .Day
                    .dayseries
                    .state_series(country, stat, "DESC", 25)
                    .datapoints
    val stateseries = statesums.map( ss => ss.state )
    var statseries: Array[Long] = Array()

    if(stat == "Deaths"){statseries = statesums.map(ss => ss.deaths)}
    else if(stat == "Recovered"){statseries = statesums.map(ss => ss.recovered)}
    else if(stat == "Confirmed"){statseries = statesums.map(ss => ss.confirmed)}

    Ok(views.html.graph.state_view(country, stateseries, statseries, stat))
  }
}
