package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import models.Users
import entities.User

object Admin extends Controller {

  val userModel = new Users

  val userCreateForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply) )

  val userLoginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    ) verifying ("Invalid username or password", result => result match {
      case (username, password) => userModel.authenticate(username, password)
    })
  )



  def index = Action {

    Ok(views.html.index("Admin index"))
  }

  def create = Action {

    Ok(views.html.admin.user.create(userCreateForm))
  }

  def save = Action { implicit request =>
    val result = userCreateForm.bindFromRequest.fold(
    {formFail => Ok(views.html.admin.user.create(formFail))},
    {user => userModel.save(user);Ok(views.html.index("Success")) }
    )

    result
  }

  def login = Action {
    Ok(views.html.admin.user.login(userLoginForm))
  }

  def process = Action {
    implicit request =>
      val result = userLoginForm.bindFromRequest.fold(
      {formFail => Ok(views.html.admin.user.login(formFail))},
      {user => Ok(views.html.index("Success")) }
      )

      result
  }

}