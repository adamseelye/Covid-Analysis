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

  def overall(stat: String, column: String, order: String, page: Int) = Action { implicit request =>
    var data = models.Day.dayseries.view_overall()
    
    if(order == "ASC"){
      data = data.order_by(column, "ASC")
    }else{
      data = data.order_by(column, "DESC")
    }
    
    Ok(views.html.test.overall(data.page(page), stat, column, order, page, data.pages()))
  }
}
