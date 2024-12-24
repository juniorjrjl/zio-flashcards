package br.com.flashcards.adapter.endpoint.request

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import sttp.tapir.Schema

case class DeckInsertRequest(
    title: String,
    description: String,
    cards: List[CardDeckInsertRequest]
)

object DeckInsertRequest:
  given encoder: Encoder[DeckInsertRequest] =
    deriveEncoder[DeckInsertRequest]

  given decoder: Decoder[DeckInsertRequest] =
    deriveDecoder[DeckInsertRequest]

  given schema: Schema[DeckInsertRequest] =
    Schema
      .derived[DeckInsertRequest]
      .modify(_.title)(_.description("Deck title"))
      .modify(_.description)(_.description("Deck description"))
      .modify(_.cards)(_.description("Cards belongs to deck"))

case class DeckUpdateRequest(
    title: String,
    description: String
)

object DeckUpdateRequest:
  given encoder: Encoder[DeckUpdateRequest] =
    deriveEncoder[DeckUpdateRequest]

  given decoder: Decoder[DeckUpdateRequest] =
    deriveDecoder[DeckUpdateRequest]

  given schema: Schema[DeckUpdateRequest] =
    Schema
      .derived[DeckUpdateRequest]
      .modify(_.title)(_.description("Deck title"))
      .modify(_.description)(_.description("Deck description"))
