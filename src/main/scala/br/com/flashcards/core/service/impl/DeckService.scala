package br.com.flashcards.core.service.impl

import br.com.flashcards.core.exception.{DeckServiceError, GenericError}
import br.com.flashcards.core.port.persistence.DeckWritePort
import br.com.flashcards.core.service.query.DeckRead
import br.com.flashcards.core.service.*
import zio.{IO, ZLayer}
import io.scalaland.chimney.dsl.*

case class DeckService(writePort: DeckWritePort, query: DeckRead)
    extends DeckWrite:

  override def insert(
      domain: InsertDeckDomain
  ): IO[DeckServiceError, InsertedDeckDomain] =
    writePort.insert(domain).mapError(_.into[GenericError].transform)

  override def update(
      domain: UpdateDeckDomain
  ): IO[DeckServiceError, UpdatedDeckDomain] =
    writePort.update(domain).mapError(_.into[GenericError].transform)

  override def delete(id: Long): IO[DeckServiceError, Unit] =
    writePort.delete(id).mapError(_.into[GenericError].transform)

object DeckService:
  val layer: ZLayer[DeckWritePort & DeckRead, DeckServiceError, DeckWrite] =
    ZLayer.fromFunction(DeckService(_, _))
