package controllers

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

// Spark
import models.DaySeries

class Country @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def view(column: String = "Country", order: String = "ASC", page: Int = 0) = Action { implicit request =>
    val countries: models.CountrySeries = models
                                          .Day
                                          .dayseries
                                          .country_series(column, order)
    Ok(views.html.country.view(countries.page(page), 
                              column, 
                              order, 
                              page, 
                              countries.pages()
      )
    )
  }

  def states(country: String = "ALL", column: String = "State", order: String = "ASC") = Action { implicit request =>
    val states = models.Day.dayseries.state_series(country, column, order)
    
    Ok(views.html.country.states(states, country, column, order))
  }
}
