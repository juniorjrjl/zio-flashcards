package br.com.flashcards.adapter.endpoint.response

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import sttp.tapir.Schema

case class DeckInsertedResponse(
    id: Long,
    title: String,
    description: String,
    cards: List[CardDeckInsertedResponse]
)

object DeckInsertedResponse:
  given encoder: Encoder[DeckInsertedResponse] =
    deriveEncoder[DeckInsertedResponse]

  given decoder: Decoder[DeckInsertedResponse] =
    deriveDecoder[DeckInsertedResponse]

  given schema: Schema[DeckInsertedResponse] =
    Schema
      .derived[DeckInsertedResponse]
      .modify(_.id)(_.description("Deck identifier"))
      .modify(_.title)(_.description("Deck title"))
      .modify(_.description)(_.description("Deck description"))
      .modify(_.cards)(_.description("Cards belongs to deck"))

case class CardDeckInsertedResponse(
    id: Long,
    front: String,
    back: String
)

object CardDeckInsertedResponse:
  given encoder: Encoder[CardDeckInsertedResponse] =
    deriveEncoder[CardDeckInsertedResponse]

  given decoder: Decoder[CardDeckInsertedResponse] =
    deriveDecoder[CardDeckInsertedResponse]

  given schema: Schema[CardDeckInsertedResponse] =
    Schema
      .derived[CardDeckInsertedResponse]
      .modify(_.id)(_.description("Card identifier"))
      .modify(_.front)(_.description("Question made by card"))
      .modify(_.back)(_.description("Answer expected by card"))

case class DeckUpdatedResponse(
                                 id: Long,
                                 title: String,
                                 description: String,
                                 cards: List[CardDeckUpdatedResponse]
                               )

object DeckUpdatedResponse:
  given encoder: Encoder[DeckUpdatedResponse] =
    deriveEncoder[DeckUpdatedResponse]

  given decoder: Decoder[DeckUpdatedResponse] =
    deriveDecoder[DeckUpdatedResponse]

  given schema: Schema[DeckUpdatedResponse] =
    Schema
      .derived[DeckUpdatedResponse]
      .modify(_.id)(_.description("Deck identifier"))
      .modify(_.title)(_.description("Deck title"))
      .modify(_.description)(_.description("Deck description"))
      .modify(_.cards)(_.description("Cards belongs to deck"))

case class CardDeckUpdatedResponse(
                                     id: Long,
                                     front: String,
                                     back: String
                                   )

object CardDeckUpdatedResponse:
  given encoder: Encoder[CardDeckUpdatedResponse] =
    deriveEncoder[CardDeckUpdatedResponse]

  given decoder: Decoder[CardDeckUpdatedResponse] =
    deriveDecoder[CardDeckUpdatedResponse]

  given schema: Schema[CardDeckUpdatedResponse] =
    Schema
      .derived[CardDeckUpdatedResponse]
      .modify(_.id)(_.description("Card identifier"))
      .modify(_.front)(_.description("Question made by card"))
      .modify(_.back)(_.description("Answer expected by card"))

