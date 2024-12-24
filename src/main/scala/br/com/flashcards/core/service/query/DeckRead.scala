package br.com.flashcards.core.service.query

import br.com.flashcards.core.exception.DeckException
import zio.IO

trait DeckRead:

  def list(): IO[DeckException, List[DeckListDomain]]

  def findById(id: Long): IO[DeckException, DeckFoundDomain]

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
