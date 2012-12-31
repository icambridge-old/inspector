package util

object Registry {

  protected[this] var data = Map[String,Any]()

  def set(varName: String, varValue: Any) = {
    data += varName -> varValue
  }

  def get(varName: String) = {
    data(varName)
  }

}