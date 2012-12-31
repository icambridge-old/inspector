package util

object Location {

  protected[this] var page: String = ""

  def set(pageName: String) = page = pageName

  def isCurrentPage(pageName: String): Boolean = (page == pageName)

  def currentPage = page

}