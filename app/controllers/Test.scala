package controllers

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

// Spark
import models.DaySeries

class Test @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def country(country: String, stat: String)= Action { implicit request =>
    // TODO: It looks like we're getting multiple inputs for the same date for
    // because of the value 
    val dayseries = models.Day.dayseries.country(country).by_date()
    Ok(views.html.test.country(dayseries, country, stat))
  }

  def state(state: String, stat: String) = Action { implicit request =>
    val dayseries = models.Day.dayseries.state(state).by_date()
    Ok(views.html.test.state(dayseries, state, stat))
  }

  // def state(string: State, stat: String) = Action { implicit request =>
  //   val stat_view: Map = Map(
  //     "deaths" -> views.html.test.state.deaths,
  //     "confirmed" -> views.html.test.state.confirmed,
  //     "recovered" -> views.html.test.state.recovered
  //   )

  //   val timeseries = models.DataPoint.timeseries.state(state).by_date()
  //   val page = stat_view(stat)

  //   Ok(page)
  // }
}
