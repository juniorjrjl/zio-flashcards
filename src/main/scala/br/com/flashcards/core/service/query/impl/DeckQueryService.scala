package br.com.flashcards.core.service.query.impl

import br.com.flashcards.core.exception.DeckServiceError
import br.com.flashcards.core.service.query.{DeckFoundDomain, DeckListDomain, DeckRead}
import zio.{IO, ZIO, ZLayer}

case class DeckQueryService() extends DeckRead:

  override def findById(
      id: Long
  ): IO[DeckServiceError, DeckFoundDomain] =
    ZIO.succeed(DeckFoundDomain(1, "title", "description", List.empty))

  override def list(): IO[DeckServiceError, List[DeckListDomain]] =
    ZIO.succeed(List.empty)

object DeckQueryService:
  val layer: ZLayer[Any, DeckServiceError, DeckRead] =
    ZLayer.succeed(DeckQueryService())
