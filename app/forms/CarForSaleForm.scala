
package forms

import models.Car
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}


object CarForSaleForm {

  val imgSrcConstraint: Constraint[List[String]] = Constraint("constraint.other")(list =>
    if(list.nonEmpty){
      Logger.error("[CarFormSaleForm] [Submit a new car for sale] someone attempted to submit imgSrc via a post")
      Invalid(Seq(ValidationError("Something has gone wrong with your request")))
    }
    else {
      Valid
    })


  val form = Form[Car](
    mapping(
      "id" -> optional(number),
      "title" -> text,
      "description" -> text,
      "mileage" -> text,
      "price" -> text,
      "imgSrc" -> list(text).verifying(imgSrcConstraint)
    )(Car.apply)(Car.unapply)
  )
}
