package models

import play.api.libs.json.Json

case class Car(title:String,
               description:String,
               mileage:String,
               price:String,
               imgSrc:List[String])

object Car {
  implicit val formats = Json.format[Car]
}