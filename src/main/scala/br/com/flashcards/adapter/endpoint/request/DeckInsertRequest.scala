package br.com.flashcards.adapter.endpoint.request

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema
import zio.prelude.Validation

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

case class CardDeckInsertRequest(
    front: String,
    back: String
)

object CardDeckInsertRequest:
  given encoder: Encoder[CardDeckInsertRequest] =
    deriveEncoder[CardDeckInsertRequest]

  given decoder: Decoder[CardDeckInsertRequest] =
    deriveDecoder[CardDeckInsertRequest]

  given schema: Schema[CardDeckInsertRequest] =
    Schema
      .derived[CardDeckInsertRequest]
      .modify(_.front)(_.description("Question made by card"))
      .modify(_.back)(_.description("Answer expected by card"))

object DeckInsertRequestValidator extends ValidationChecker[DeckInsertRequest]:

  private def validateTitle(
      request: DeckInsertRequest
  ): Validation[ValidationError, DeckInsertRequest] =
    Validation.validateWith(
      validateStringBlank(
        request.title,
        "title",
        "Informe um título válido",
        request
      ),
      validateStringLength(
        request.title,
        3,
        150,
        "title",
        "O título deve ter entre 3 e 150 caracteres",
        request
      )
    )((a, b) => request)

  private def validateDescription(
      request: DeckInsertRequest
  ): Validation[ValidationError, DeckInsertRequest] =
    Validation.validateWith(
      validateStringBlank(
        request.description,
        "description",
        "Informe uma descrição válidá",
        request
      ),
      validateStringLength(
        request.description,
        3,
        150,
        "description",
        "A descrição deve ter entre 3 e 150 caracteres",
        request
      )
    )((a, b) => request)

  private def validateCards(
      request: DeckInsertRequest
  ): Validation[ValidationError, DeckInsertRequest] =
    validateIterableNotNullAndMinSize(
      request.cards,
      1,
      "cards",
      "O deck deve ter pelo menos 1 card",
      request
    )

  private def validateCardFront(
      front: String,
      request: DeckInsertRequest
  ): Validation[ValidationError, DeckInsertRequest] =
    Validation.validateWith(
      validateStringBlank(
        front,
        "front",
        "Informe a pergunta do card",
        request
      ),
      validateStringLength(
        front,
        3,
        150,
        "front",
        "A pergunta do card deve ter entre 3 e 150 caracteres",
        request
      )
    )((a, b) => request)

  private def validateCardBack(
      back: String,
      request: DeckInsertRequest
  ): Validation[ValidationError, DeckInsertRequest] =
    Validation.validateWith(
      validateStringBlank(back, "back", "Informe a resposta do card", request),
      validateStringLength(
        back,
        3,
        150,
        "back",
        "A resposta do card deve ter entre 3 e 150 caracteres",
        request
      )
    )((a, b) => request)

  override def validate(
      request: DeckInsertRequest
  ): Validation[ValidationError, DeckInsertRequest] =
    val validations =
      List(
        validateTitle(request),
        validateDescription(request),
        validateCards(request)
      ) ++ request.cards.map(c =>
        validateCardFront(c.front, request)
      ) ++ request.cards.map(c => validateCardBack(c.back, request))

    Validation.validateAll(validations).as(request)
