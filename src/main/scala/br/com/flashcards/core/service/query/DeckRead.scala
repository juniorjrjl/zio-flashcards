package br.com.flashcards.core.service.query

import br.com.flashcards.core.exception.DeckServiceError
import zio.IO

trait DeckRead:

  def list(): IO[DeckServiceError, List[DeckListDomain]]

  def findById(id: Long): IO[DeckServiceError, DeckFoundDomain]

case class DeckListDomain(
    id: Long,
    title: String,
    description: String
)

case class DeckFoundDomain(
    id: Long,
    title: String,
    description: String,
    cards: List[CardDeckFoundDomain]
)

case class CardDeckFoundDomain(
    id: Long,
    front: String,
    back: String
)
