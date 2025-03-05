package br.com.flashcards.adapter.endpoint.response.error

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import java.time.OffsetDateTime

sealed trait HttpError:
  val message: String
  val details: String
  val statusCode: Int
  val datetime: OffsetDateTime

object HttpError:

  sealed case class GenericError(
      message: String,
      details: String,
      statusCode: Int = 500,
      datetime: OffsetDateTime = OffsetDateTime.now()
  ) extends HttpError
  object GenericError:
    given encoder: Encoder[GenericError] =
      deriveEncoder[GenericError]

    given decoder: Decoder[GenericError] =
      deriveDecoder[GenericError]

  case class RequestError(field: String, error: String)
  object RequestError:
    given encoder: Encoder[RequestError] =
      deriveEncoder[RequestError]

    given decoder: Decoder[RequestError] =
      deriveDecoder[RequestError]
  
  sealed case class BadRequestError(
      message: String,
      details: String,
      errors: List[RequestError],
      statusCode: Int = 400,
      datetime: OffsetDateTime = OffsetDateTime.now()
  ) extends HttpError

  object BadRequestError:
    given encoder: Encoder[BadRequestError] =
      deriveEncoder[BadRequestError]

    given decoder: Decoder[BadRequestError] =
      deriveDecoder[BadRequestError]

  sealed case class NotFoundError(
      message: String,
      details: String,
      statusCode: Int = 404,
      datetime: OffsetDateTime = OffsetDateTime.now()
  ) extends HttpError

  object NotFoundError:
    given encoder: Encoder[NotFoundError] =
      deriveEncoder[NotFoundError]

    given decoder: Decoder[NotFoundError] =
      deriveDecoder[NotFoundError]
