package controllers

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

// Spark
import models.DaySeries

class Test @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  // def country(country: String, stat: String) = Action { implicit request => 
  //   val dayseries = models.Day.dayseries.view_country(country).by_date()
  //   Ok(views.html.graph.country(dayseries, country, stat))
  // }

  // def state(country: String, state: String, stat: String) = Action { implicit request =>
  //   val dayseries = models.Day.dayseries
  //                     .country(country)
  //                     .state(state).by_date()
  //   Ok(views.html.graph.state(dayseries, state, stat))
  // }

  def overall(stat: String) = Action { implicit request =>
    val dayseries = models.Day.dayseries.view_overall().by_date()
    Ok(views.html.graph.overall(dayseries, stat))
  }
}
