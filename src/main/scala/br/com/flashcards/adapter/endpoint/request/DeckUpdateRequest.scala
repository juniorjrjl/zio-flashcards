package br.com.flashcards.adapter.endpoint.request

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema

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
