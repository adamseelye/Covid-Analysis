package controllers

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

// Spark
import models.DaySeries

class Test @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def countryDeaths(country: String) = Action { implicit request =>
    val dayseries = models.Day.dayseries.country(country).by_date()
    Ok(views.html.test.country.deaths(dayseries, country))
  }
  // def country(country: String, stat: String) = Action { implicit request =>
  //   val stat_view = Map(
  //     "deaths" -> views.html.test.country.deaths,
  //     "confirmed" -> views.html.test.country.confirmed,
  //     "recovered" -> views.html.test.country.recovered
  //   )
  //   val timeseries = models.DataPoint.timeseries.country(country).by_date()

  //   val page = stat_view(stat)
  //   Ok(page)
  // }

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
