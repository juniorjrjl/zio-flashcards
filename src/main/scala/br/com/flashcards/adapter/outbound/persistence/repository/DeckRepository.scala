package br.com.flashcards.adapter.outbound.persistence.repository

import br.com.flashcards.adapter.outbound.persistence.entity.{
  CardEntity,
  DeckEntity
}
import br.com.flashcards.adapter.outbound.persistence.exception.DeckRepositoryError
import br.com.flashcards.config.DBContext.*
import br.com.flashcards.core.port.persistence.DeckWritePort
import br.com.flashcards.core.service.{
  InsertDeckDomain,
  InsertedCardDeckDomain,
  InsertedDeckDomain,
  UpdateDeckDomain,
  UpdatedDeckDomain
}
import io.getquill.*
import io.getquill.jdbczio.Quill
import io.scalaland.chimney.dsl.*
import zio.{IO, ZIO, ZLayer}

import java.sql.SQLException
import java.time.OffsetDateTime

case class DeckRepository(quill: Quill.Postgres[SnakeCase])
    extends DeckWritePort:
  import quill.*

  private inline def deckQuery = quote(
    querySchema[DeckEntity](entity = "decks")
  )

  private inline def cardQuery = quote(
    querySchema[CardEntity](entity = "cards")
  )

  override def insert(
      domain: InsertDeckDomain
  ): IO[DeckRepositoryError, InsertedDeckDomain] =
    ZIO
      .scoped {
        transaction {
          for {
            deck <- run(
              deckQuery
                .insert(
                  _.title -> lift(domain.title),
                  _.description -> lift(domain.description)
                )
                .returning(e => e)
            )
            cards <- ZIO.foreach(domain.cards) { card =>
              run(
                cardQuery
                  .insert(
                    _.back -> lift(card.back),
                    _.front -> lift(card.front),
                    _.deckId -> lift(deck.id)
                  )
                  .returning(e => e)
              )
            }
            cardDomain = cards.map(_.into[InsertedCardDeckDomain].transform)
            deckWithCards = deck
              .into[InsertedDeckDomain]
              .withFieldConst(
                _.cards,
                cardDomain
              )
              .transform
          } yield deckWithCards
        }
      }
      .mapError(toRepositoryError)

  override def update(
      domain: UpdateDeckDomain
  ): IO[DeckRepositoryError, UpdatedDeckDomain] =
    run(
      deckQuery
        .filter(_.id == lift(domain.id))
        .update(
          _.title -> lift(domain.title),
          _.description -> lift(domain.description),
          _.updatedAt -> lift(OffsetDateTime.now())
        )
        .returning(e => e)
    ).mapBoth(
      toRepositoryError(_),
      _.into[UpdatedDeckDomain].withFieldConst(_.cards, List.empty).transform
    )

  override def delete(id: Long): IO[DeckRepositoryError, Unit] =
    run(deckQuery.filter(_.id == lift(id)).delete).unit
      .mapError(toRepositoryError(_))

  private def toRepositoryError(ex: Throwable): DeckRepositoryError =
    val callstack = ex.getStackTrace.map(_.toString).toList
    DeckRepositoryError(ex.getClass.getName, ex.getMessage, callstack)

object DeckRepository:

  val layer: ZLayer[Quill.Postgres[SnakeCase], Nothing, DeckWritePort] =
    ZLayer.fromFunction(DeckRepository(_))
