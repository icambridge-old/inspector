package controllers

import play.api._
import play.api.mvc._

import models.Posts

object Application extends Controller {
  
  def index = Action {

    val postModel = new Posts
    val posts = postModel.getLatest
    Ok(views.html.index("Your new application is ready!"))
  }
  
}