package br.com.flashcards.adapter.endpoint.doc

import br.com.flashcards.adapter.endpoint.request.{
  CardDeckInsertRequest,
  DeckInsertRequest,
  DeckUpdateRequest
}
import br.com.flashcards.adapter.endpoint.response.{
  CardDeckDetailsResponse,
  CardDeckInsertedResponse,
  CardDeckUpdatedResponse,
  DeckDetailsResponse,
  DeckInsertedResponse,
  DeckListResponse,
  DeckUpdatedResponse
}
import br.com.flashcards.adapter.endpoint.response.error.HttpError
import sttp.model.StatusCode.{Created, InternalServerError, NoContent, NotFound}
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.jsonBody

object DeckDocEndpoint:

  def endpoints() =
    List(
      listEndpoint,
      findByIdEndpoint,
      insertEndpoint,
      updateEndpoint,
      deleteEndpoint
    )

  val listEndpoint: Endpoint[Unit, Unit, HttpError, List[
    DeckListResponse
  ], Any] =
    endpoint.get
      .in("decks")
      .tag("Decks")
      .description("Getting decks by conditions")
      .out(jsonBody[List[DeckListResponse]].description("Deck list"))
      .errorOut(
        oneOf(
          oneOfVariant[HttpError.GenericError](
            statusCode(InternalServerError)
              .and(jsonBody[HttpError.GenericError])
          ),
          oneOfVariant[HttpError.BadRequestError](
            statusCode(NotFound).and(jsonBody[HttpError.BadRequestError])
          )
        )
      )

  val findByIdEndpoint
      : Endpoint[Unit, Long, HttpError, DeckDetailsResponse, Any] =
    endpoint.get
      .in("decks" / path[Long]("id"))
      .tag("Decks")
      .description("Getting deck by identifier")
      .out(
        jsonBody[DeckDetailsResponse]
          .description("Requested Deck")
          .example(
            DeckDetailsResponse(
              1,
              "Spanish Initial Deck",
              "Spanish introduction",
              List(CardDeckDetailsResponse(1, "Lunes", "Monday"))
            )
          )
      )
      .errorOut(
        oneOf(
          oneOfVariant[HttpError.GenericError](
            statusCode(InternalServerError)
              .and(jsonBody[HttpError.GenericError])
          ),
          oneOfVariant[HttpError.NotFoundError](
            statusCode(NotFound).and(jsonBody[HttpError.NotFoundError])
          )
        )
      )

  val insertEndpoint: Endpoint[
    Unit,
    DeckInsertRequest,
    HttpError,
    DeckInsertedResponse,
    Any
  ] =
    endpoint.post
      .in("decks")
      .tag("Decks")
      .description("Getting deck by identifier")
      .in(
        jsonBody[DeckInsertRequest]
          .description("Deck to insert")
          .example(
            DeckInsertRequest(
              "Spanish Initial Deck",
              "Spanish introduction",
              List(CardDeckInsertRequest("Lunes", "Monday"))
            )
          )
      )
      .out(
        statusCode(Created) and
          jsonBody[DeckInsertedResponse]
            .description("Inserted Deck")
            .example(
              DeckInsertedResponse(
                1,
                "Spanish Initial Deck",
                "Spanish introduction",
                List(CardDeckInsertedResponse(1, "Lunes", "Monday"))
              )
            )
      )
      .errorOut(
        oneOf(
          oneOfVariant[HttpError.GenericError](
            statusCode(InternalServerError)
              .and(jsonBody[HttpError.GenericError])
          ),
          oneOfVariant[HttpError.BadRequestError](
            statusCode(NotFound).and(jsonBody[HttpError.BadRequestError])
          )
        )
      )

  val updateEndpoint: Endpoint[
    Unit,
    (Long, DeckUpdateRequest),
    HttpError,
    DeckUpdatedResponse,
    Any
  ] =
    endpoint.put
      .in("decks" / path[Long]("id"))
      .tag("Decks")
      .description("Update deck using identifier")
      .in(
        jsonBody[DeckUpdateRequest]
          .description("Requested Deck")
          .example(
            DeckUpdateRequest(
              "Spanish Initial Deck",
              "Spanish introduction"
            )
          )
      )
      .out(
        jsonBody[DeckUpdatedResponse]
          .description("Requested Deck")
          .example(
            DeckUpdatedResponse(
              1,
              "Spanish Initial Deck",
              "Spanish introduction",
              List(CardDeckUpdatedResponse(1, "Lunes", "Monday"))
            )
          )
      )
      .errorOut(
        oneOf(
          oneOfVariant[HttpError.GenericError](
            statusCode(InternalServerError)
              .and(jsonBody[HttpError.GenericError])
          ),
          oneOfVariant[HttpError.NotFoundError](
            statusCode(NotFound).and(jsonBody[HttpError.NotFoundError])
          )
        )
      )

  val deleteEndpoint: Endpoint[Unit, Long, HttpError, Unit, Any] =
    endpoint.delete
      .in("decks" / path[Long]("id"))
      .tag("Decks")
      .description("Deleting deck by identifier")
      .out(statusCode(NoContent))
      .errorOut(
        oneOf(
          oneOfVariant[HttpError.GenericError](
            statusCode(InternalServerError)
              .and(jsonBody[HttpError.GenericError])
          ),
          oneOfVariant[HttpError.NotFoundError](
            statusCode(NotFound).and(jsonBody[HttpError.NotFoundError])
          )
        )
      )
