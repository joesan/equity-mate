package com.bigelectrons.equitymate.core

import com.typesafe.config.ConfigFactory
import com.typesafe.
import play.api.{Application, BuiltInComponentsFromContext, Configuration, _}
import play.api.ApplicationLoader.Context

// these two imports below are needed for the routes resolution
import play.api.routing.Router
import scala.concurrent.Future

/**
  * Bootstrap the application by performing a compile time DI
  */
final class Bootstrap extends ApplicationLoader with LazyLogging {

  private[this] class App(context: Context)
    extends BuiltInComponentsFromContext(context)
      with _root_.controllers.AssetsComponents {

    // We use the Monix Scheduler
    implicit val s = monix.execution.Scheduler.Implicits.global

    def stop(bindings: AppBindings) = {
      logger.info("Stopping application :: plant-simulator")
      bindings.globalChannel.onComplete()
    }

    def start = {
      logger.info("Starting application :: plant-simulator")
      AppBindings(actorSystem, materializer)
    }

    // 0. Set the filters
    override lazy val httpFilters = Seq(new LoggingFilter())

    // 1. create the dependencies that will be injected
    lazy val appBindings = start

    // 2. inject the dependencies into the controllers
    // TODO: The dependencies below are for Swagger UI, which is not working at the moment!!!!
    //lazy val apiHelpController = new ApiHelpController(DefaultControllerComponents)
    //lazy val webJarAssets = new WebJarAssets(httpErrorHandler, configuration, environment)
    lazy val applicationController =
    new ApplicationController(appBindings.appConfig, controllerComponents)
    lazy val powerPlantController =
      new PowerPlantController(appBindings, controllerComponents)
    lazy val powerPlantOpsController =
      new PowerPlantOperationsController(appBindings, controllerComponents)
    //lazy val assets = new Assets(httpErrorHandler)

    // 3. Setup the Routes
    override def router: Router = new Routes(
      httpErrorHandler,
      assets,
      applicationController,
      powerPlantController,
      powerPlantOpsController
      //apiHelpController,
      //webJarAssets
    )

    // 4. add the shutdown hook to properly dispose all connections
    applicationLifecycle.addStopHook { () =>
      Future(stop(appBindings))
    }
  }

  override def load(context: Context): Application = {
    val configuration = Configuration(ConfigFactory.load())

    val newContext = context.copy(initialConfiguration = configuration)
    LoggerConfigurator(newContext.environment.classLoader)
      .foreach(_.configure(newContext.environment))

    new App(newContext).application
  }
}
