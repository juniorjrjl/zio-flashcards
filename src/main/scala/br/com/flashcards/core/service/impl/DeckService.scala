package br.com.flashcards.core.service.impl

import br.com.flashcards.core.exception.DeckException
import br.com.flashcards.core.service.query.DeckRead
import br.com.flashcards.core.service.{
  DeckWrite,
  InsertDeckDomain,
  InsertedCardDeckDomain,
  InsertedDeckDomain,
  UpdateDeckDomain,
  UpdatedDeckDomain
}
import io.scalaland.chimney.dsl.*
import zio.{IO, ZIO, ZLayer}

case class DeckService(query: DeckRead) extends DeckWrite:

  override def insert(
      domain: InsertDeckDomain
  ): IO[DeckException, InsertedDeckDomain] =
    ZIO.succeed {
      domain
        .into[InsertedDeckDomain]
        .withFieldConst(_.id, 1L)
        .withFieldComputed(
          _.cards,
          _.cards
            .map(
              _.into[InsertedCardDeckDomain].withFieldConst(_.id, 1L).transform
            )
        )
        .transform
    }

  override def update(
      domain: UpdateDeckDomain
  ): IO[DeckException, UpdatedDeckDomain] = ZIO.succeed {
    domain
      .into[UpdatedDeckDomain]
      .withFieldConst(_.cards, List.empty)
      .transform
  }

  override def delete(id: Long): IO[DeckException, Unit] = ZIO.unit

object DeckService:
  val layer: ZLayer[DeckRead, DeckException, DeckWrite] =
    ZLayer.fromFunction(DeckService(_))
