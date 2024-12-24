package br.com.flashcards.config

import br.com.flashcards.adapter.endpoint.DeckEndpoint
import br.com.flashcards.adapter.endpoint.doc.DeckDocEndpoint
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zio.{Task, ZLayer}

case class EndpointConfig(deckRoute: DeckEndpoint):

  val swaggerEndpoints: List[ServerEndpoint[Any, Task]] =
    SwaggerInterpreter().fromEndpoints[Task](
      DeckDocEndpoint.endpoints(),
      "Flashcards",
      "1.0"
    )

  val endpoints = deckRoute.endpoints ++ swaggerEndpoints

object EndpointConfig:
  val layer: ZLayer[DeckEndpoint, Nothing, EndpointConfig] =
    ZLayer.fromFunction(EndpointConfig(_))
