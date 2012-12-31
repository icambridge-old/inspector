package controllers.backend

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import models.Pages
import util.Location

object Page extends Controller {

  val pageModel = new Pages

  val pageForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "body" -> nonEmptyText,
      "slug" -> nonEmptyText, // TODO make optional
      "id" -> optional(number)
    )(entities.Page.apply)(entities.Page.unapply))

  Location.set("Blog")

  def list = Action {
    request =>
      request.session.get("username").map {
        user =>
          val pages = pageModel.getAll
          Ok(views.html.admin.pages.list(pages, user))
      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }

  def create = Action {
    request =>
      request.session.get("username").map {
        user =>
          Ok(views.html.admin.pages.edit(pageForm, user))
      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }

  def edit(slug: String) = Action {
    request =>
      request.session.get("username").map {
        user =>

          val pageOption = pageModel.getPage(slug)

          pageOption match {
            case Some(page) => {
              val filled = pageForm.fill(page)
              Ok(views.html.admin.pages.edit(filled, user))
            }
            case None => Ok(views.html.blog.notfound())
          }

      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }

  def save = Action {
    implicit request =>

      request.session.get("username").map {
        user =>

          val result = pageForm.bindFromRequest.fold(
          {
            formFail => Ok(views.html.admin.pages.edit(formFail, user))
          }, {
            page => pageModel.save(page); Ok(views.html.page.view(page))
          }
          )

          result

      }.getOrElse {
        Unauthorized("Oops, you are not connected")
      }
  }


}