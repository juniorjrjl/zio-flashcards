package br.com.flashcards.adapter.endpoint.response

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema
import sttp.tapir.generic.auto._

case class DeckListResponse(
    id: Long,
    title: String,
    description: String
)

object DeckListResponse:
  given encoder: Encoder[DeckListResponse] = deriveEncoder[DeckListResponse]

  given decoder: Decoder[DeckListResponse] = deriveDecoder[DeckListResponse]

  given schema: Schema[DeckDetailsResponse] =
    Schema
      .derived[DeckDetailsResponse]
      .modify(_.id)(_.description("Deck identifier"))
      .modify(_.title)(
        _.description("Deck title")
      )
      .modify(_.description)(
        _.description("Deck description")
      )

case class DeckDetailsResponse(
    id: Long,
    title: String,
    description: String,
    cards: List[CardDeckDetailsResponse]
)

object DeckDetailsResponse:
  given encoder: Encoder[DeckDetailsResponse] =
    deriveEncoder[DeckDetailsResponse]

  given decoder: Decoder[DeckDetailsResponse] =
    deriveDecoder[DeckDetailsResponse]

  given schema: Schema[DeckDetailsResponse] =
    Schema
      .derived[DeckDetailsResponse]
      .modify(_.id)(_.description("Deck identifier"))
      .modify(_.title)(_.description("Deck title"))
      .modify(_.description)(_.description("Deck description"))
      .modify(_.cards)(_.description("Cards belongs to deck"))

case class CardDeckDetailsResponse(
    id: Long,
    front: String,
    back: String
)

object CardDeckDetailsResponse:
  given encoder: Encoder[CardDeckDetailsResponse] =
    deriveEncoder[CardDeckDetailsResponse]

  given decoder: Decoder[CardDeckDetailsResponse] =
    deriveDecoder[CardDeckDetailsResponse]

  given schema: Schema[CardDeckDetailsResponse] =
    Schema
      .derived[CardDeckDetailsResponse]
      .modify(_.id)(_.description("Card identifier"))
      .modify(_.front)(_.description("Question made by card"))
      .modify(_.back)(_.description("Answer expected by card"))
