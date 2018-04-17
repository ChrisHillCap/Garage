
package services

import models.Car
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.Environment
import play.api.libs.json.{JsObject, Json}
class CarsForSaleServiceSpec extends PlaySpec with MockitoSugar {

  val mockEnv: Environment = mock[Environment]

  class Setup {
    val car1 = Car(Some(1), "Ford Mondeo", "a car", "123456", "123.5", Nil)
    val car2 = Car(Some(2), "Volkswagen Polo", "another car", "654321", "123.4", Nil)

    val carsForSaleService = new CarsForSaleService {
      override val path: String = ""
      override val env: Environment = mockEnv
      override def getCarsForSale: List[Car] = {
        car1 :: car2 :: Nil
      }
      override def readFromFile: String = Json.parse("""
          |[{ "id":1,"title":"Ford Mondeo","description":"a car","mileage":"123456","price":"123.5","imgSrc":[]
          |   },
          |   { "id":2,"title":"Volkswagen Polo","description":"another car","mileage":"654321","price":"123.4","imgSrc":[]
          |   }
          | ]
        """.stripMargin).toString

      override def overWriteFile(list: List[JsObject]): String = "foo"
    }
  }

      "getCarsFromFileAndRemoveCurrent" should {
        "remove current entry and return a list of cars" in new Setup {
          val res = carsForSaleService.getCarsFromFileAndRemoveCurrent(1)
            val car1Js = Json.toJson[Car](car1)
            val car2js = Json.toJson[Car](car2)
          val carJsList =  car2js :: Nil
          res mustBe carJsList
        }
      }
  "deleteCar" should {
    "remove and write the list of cars to cars.json" in new Setup {
      val res = carsForSaleService.deleteCar(1)
      res mustBe "foo"
    }

  }

    }

