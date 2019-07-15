package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.AhcWSClient
import scala.concurrent.duration._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def test1() = Action.async { implicit request: Request[AnyContent] =>
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    val wsClient = AhcWSClient()

    wsClient.url("http://httpbin.org/post")
      .post("This is expected to be sent back as part of response body") map { resp =>
      Ok(s"resp=$resp, ${resp.body}")
    }
  }

  def test2() = Action.async { implicit request: Request[AnyContent] =>
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    val wsClient = AhcWSClient()

    wsClient.url("http://httpbin.org/post")
            .addHttpHeaders("Content-Length" -> "200")
      .post("This is expected to be sent back as part of response body") map { resp =>
      Ok(s"resp=$resp, ${resp.body}")
    }
  }
}
