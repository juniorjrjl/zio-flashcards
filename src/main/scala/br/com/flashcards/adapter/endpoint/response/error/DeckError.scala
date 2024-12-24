package br.com.flashcards.adapter.endpoint.response.error

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import java.time.OffsetDateTime

sealed trait DeckError
object DeckError:

  sealed case class GenericError(
      message: String,
      details: String,
      statusCode: Int = 500,
      datetime: OffsetDateTime
  ) extends DeckError
  object GenericError:
    given encoder: Encoder[GenericError] =
      deriveEncoder[GenericError]

    given decoder: Decoder[GenericError] =
      deriveDecoder[GenericError]

  sealed case class NotFoundError(
      message: String,
      details: String,
      statusCode: Int = 404,
      datetime: OffsetDateTime
  ) extends DeckError
  
  object NotFoundError:
    given encoder: Encoder[NotFoundError] =
      deriveEncoder[NotFoundError]
  
    given decoder: Decoder[NotFoundError] =
      deriveDecoder[NotFoundError]
