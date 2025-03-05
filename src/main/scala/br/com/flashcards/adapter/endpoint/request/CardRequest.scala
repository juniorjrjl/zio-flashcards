package br.com.flashcards.adapter.endpoint.request

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import sttp.tapir.Schema

case class CardDeckUpdateRequest(
    front: String,
    back: String
)

object CardDeckUpdateRequest:
  given encoder: Encoder[CardDeckUpdateRequest] =
    deriveEncoder[CardDeckUpdateRequest]

  given decoder: Decoder[CardDeckUpdateRequest] =
    deriveDecoder[CardDeckUpdateRequest]

  given schema: Schema[CardDeckUpdateRequest] =
    Schema
      .derived[CardDeckUpdateRequest]
      .modify(_.front)(_.description("Question made by card"))
      .modify(_.back)(_.description("Answer expected by card"))
