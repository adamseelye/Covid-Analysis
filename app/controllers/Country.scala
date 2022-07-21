package controllers

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

// Spark
import models.DaySeries

class Country @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def view() = Action { implicit request =>
    val countries = models.Day.dayseries.country_sums()
    Ok(views.html.country.view(countries))
  }
}
