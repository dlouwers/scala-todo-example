package acme.todo.server

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._

import scala.concurrent.Future

trait DIYRouting {

  def runRoute(f: => PartialFunction[HttpRequest, Future[HttpResponse]])(implicit request: HttpRequest): Future[HttpResponse] = {
    f.applyOrElse(request, notFound)
  }

  def complete( f: => Future[HttpResponse]): PartialFunction[HttpRequest, Future[HttpResponse]] = {
    case _ => f
  }

  implicit class PFSugar[T](option: PartialFunction[T, Future[HttpResponse]]) {
    def ~(fallback: => PartialFunction[T, Future[HttpResponse]]): PartialFunction[T, Future[HttpResponse]] = option.orElse(fallback)
  }

  val notFound: PartialFunction[HttpRequest, Future[HttpResponse]] = {
    case _: HttpRequest =>
      Future.successful(HttpResponse(404, entity = "Unknown resource!"))
  }

  def withMethod(httpMethod: HttpMethod)(f: PartialFunction[HttpRequest, Future[HttpResponse]])(implicit request: HttpRequest): PartialFunction[HttpRequest, Future[HttpResponse]] = {
    case request if request.method == httpMethod && f.isDefinedAt(request) => f(request)
  }

  def get(f: => PartialFunction[HttpRequest, Future[HttpResponse]])(implicit request: HttpRequest) = withMethod(GET)(f)

  def path(path: String)(f: PartialFunction[HttpRequest, Future[HttpResponse]])(implicit request: HttpRequest): PartialFunction[HttpRequest, Future[HttpResponse]] = {
    case request if request.uri.path == Uri.Path(path) && f.isDefinedAt(request) => f(request)
  }
}
