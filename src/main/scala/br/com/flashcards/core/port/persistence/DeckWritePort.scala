package br.com.flashcards.core.port.persistence

import br.com.flashcards.adapter.outbound.persistence.entity.DeckEntity
import br.com.flashcards.adapter.outbound.persistence.exception.DeckRepositoryError
import br.com.flashcards.core.exception.DeckServiceError
import br.com.flashcards.core.service.{InsertDeckDomain, InsertedDeckDomain, UpdateDeckDomain, UpdatedDeckDomain}
import zio.{IO, ZIO}

import java.sql.SQLException
import javax.sql.DataSource

trait DeckWritePort:

  def insert(entity: InsertDeckDomain): IO[DeckRepositoryError, InsertedDeckDomain]

  def update(entity: UpdateDeckDomain): IO[DeckRepositoryError, UpdatedDeckDomain]

  def delete(id: Long): IO[DeckRepositoryError, Unit]
