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
    )(User.apply)(User.unapply))

  val userLoginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    ) verifying("Invalid username or password", result => result match {
      case (username, password) => userModel.authenticate(username, password)
    })
  )


  def index = Action {
    request =>
      request.session.get("username").map {
        user =>
          Ok(views.html.admin.index("Admin index", user))
      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }

  def create = Action {
    request =>
      request.session.get("username").map {
        user =>
          Ok(views.html.admin.user.create(userCreateForm, user))
      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }

  def save = Action {
    implicit request =>
      request.session.get("username").map {
        user =>

          val result = userCreateForm.bindFromRequest.fold(
          {
            formFail => Ok(views.html.admin.user.create(formFail, user))
          }, {
            user => userModel.save(user); Ok(views.html.index("Success"))
          }
          )

          result

      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }

  def login = Action {
    Ok(views.html.admin.user.login(userLoginForm))
  }

  def process = Action {
    implicit request =>
      val result = userLoginForm.bindFromRequest.fold(
      {
        formFail => Ok(views.html.admin.user.login(formFail))
      }, {
        user => Redirect(routes.Admin.index).withSession("username" -> user._1)
      }
      )

      result
  }

}