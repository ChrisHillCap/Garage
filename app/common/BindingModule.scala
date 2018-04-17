package common


import com.google.inject.AbstractModule
import controllers.{FileServingController, FileServingControllerImpl, MainController, MainControllerImpl}
import services.{CarsForSaleService, CarsForSaleServiceImpl}

class BindingModule extends AbstractModule {
  override def configure(): Unit = {
    bind()
  }

  def bind(): Unit = {
    bind(classOf[MainController]).to(classOf[MainControllerImpl]).asEagerSingleton()
    bind(classOf[CarsForSaleService]).to(classOf[CarsForSaleServiceImpl]).asEagerSingleton()
    bind(classOf[FileServingController]).to(classOf[FileServingControllerImpl]).asEagerSingleton()
  }
}
