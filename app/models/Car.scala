package models

import play.api.libs.functional.syntax.unlift
import play.api.libs.json._

case class Car(id:Option[Int],
               title:String,
               description:String,
               mileage:String,
               price:String,
               imgSrc:List[String])

object Car {
  implicit val formats = Json.format[Car]

  def getJsonArray(list: List[JsObject]): JsArray = {
    list.foldLeft(JsArray())((acc, x) => acc ++ Json.arr(x))
  }
}