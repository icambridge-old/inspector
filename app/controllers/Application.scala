package controllers

import play.api._
import play.api.mvc._

import util.Location

object Application extends Controller {

  def index = Action {
    Location.set("Home")
    Ok(views.html.index("Your new application is ready!"))
  }

}