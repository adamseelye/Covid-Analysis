package controllers

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

// Spark
import models.DaySeries

class TestController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def country(country: String, stat: String) = Action { implicit request => 
    val dayseries = models.Day.dayseries.view_country(country).by_date()
    Ok(views.html.test.country(dayseries, country, stat))
  }

  def state(country: String, state: String, stat: String) = Action { implicit request =>
    val dayseries = models.Day.dayseries
                      .country(country)
                      .state(state).by_date()
    Ok(views.html.test.state(dayseries, state, stat))
  }

  def overall(stat: String) = Action { implicit request =>
    val dayseries = models.Day.dayseries.view_overall().by_date()
    Ok(views.html.test.overall(dayseries, stat))
  }
}
