package common


import com.google.inject.AbstractModule
import controllers.{MainController, MainControllerImpl}

class BindingModule extends AbstractModule {
  override def configure(): Unit = {
    bindControllers()
  }

  def bindControllers(): Unit = {
    bind(classOf[MainController]).to(classOf[MainControllerImpl]).asEagerSingleton()
  }
}
