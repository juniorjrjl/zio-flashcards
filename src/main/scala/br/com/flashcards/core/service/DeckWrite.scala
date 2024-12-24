package br.com.flashcards.core.service

import br.com.flashcards.core.exception.DeckException
import zio.IO

trait DeckWrite:

  def insert(
      domain: InsertDeckDomain
  ): IO[DeckException, InsertedDeckDomain]

  def update(
      domain: UpdateDeckDomain
  ): IO[DeckException, UpdatedDeckDomain]

  def delete(id: Long): IO[DeckException, Unit]

case class InsertDeckDomain(
    title: String,
    description: String,
    cards: List[InsertCardDeckDomain]
)

case class InsertCardDeckDomain(front: String, back: String)

case class InsertedDeckDomain(
    id: Long,
    title: String,
    description: String,
    cards: List[InsertedCardDeckDomain]
)

case class InsertedCardDeckDomain(id: Long, front: String, back: String)

case class UpdateDeckDomain(id: Long, title: String, description: String)

case class UpdatedDeckDomain(
    id: Long,
    title: String,
    description: String,
    cards: List[UpdatedCardDeckDomain]
)

case class UpdatedCardDeckDomain(id: Long, front: String, back: String)
