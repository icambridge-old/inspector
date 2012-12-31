package controllers

import play.api._
import play.api.mvc._
import util.Location

object Page extends Controller {

  val pageModel = new models.Pages


  def page(slug: String) = Action {

    val pageOption = pageModel.getPage(slug)

    pageOption match {
      case Some(pageEntity) => {
        Location.set(pageEntity.title)
        Ok(views.html.page.view(pageEntity))
      }
      case None => Ok(views.html.page.notfound())
    }

  }

}