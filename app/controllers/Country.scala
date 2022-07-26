package controllers

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

// Spark
import models.DaySeries

class Country @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def view(column: String = "Country", order: String = "ASC") = Action { implicit request =>
    val countries = models.Day.dayseries.country_sums(column, order)
    Ok(views.html.country.view(countries, column, order))
  }

  def states(country: String = "ALL", column: String = "State", order: String = "ASC") = Action { implicit request =>
    val states = models.Day.dayseries.state_sums(country, column, order)
    Ok(views.html.country.states(states, country, column, order))
  }
}
