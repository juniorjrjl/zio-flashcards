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
import br.com.flashcards.adapter.endpoint.response.error.DeckError
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

  val listEndpoint: Endpoint[Unit, Unit, DeckError.GenericError, List[
    DeckListResponse
  ], Any] =
    endpoint.get
      .in("decks")
      .tag("Decks")
      .description("Getting decks by conditions")
      .out(jsonBody[List[DeckListResponse]].description("Deck list"))
      .errorOut(
        statusCode(InternalServerError)
          .and(jsonBody[DeckError.GenericError])
      )

  val findByIdEndpoint
      : Endpoint[Unit, Long, DeckError, DeckDetailsResponse, Any] =
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
          oneOfVariant[DeckError.GenericError](
            statusCode(InternalServerError)
              .and(jsonBody[DeckError.GenericError])
          ),
          oneOfVariant[DeckError.NotFoundError](
            statusCode(NotFound).and(jsonBody[DeckError.NotFoundError])
          )
        )
      )

  val insertEndpoint: Endpoint[
    Unit,
    DeckInsertRequest,
    DeckError.GenericError,
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
        statusCode(InternalServerError)
          .and(jsonBody[DeckError.GenericError])
      )

  val updateEndpoint: Endpoint[
    Unit,
    (Long, DeckUpdateRequest),
    DeckError,
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
          oneOfVariant[DeckError.GenericError](
            statusCode(InternalServerError)
              .and(jsonBody[DeckError.GenericError])
          ),
          oneOfVariant[DeckError.NotFoundError](
            statusCode(NotFound).and(jsonBody[DeckError.NotFoundError])
          )
        )
      )

  val deleteEndpoint: Endpoint[Unit, Long, DeckError, Unit, Any] =
    endpoint.delete
      .in("decks" / path[Long]("id"))
      .tag("Decks")
      .description("Deleting deck by identifier")
      .out(statusCode(NoContent))
      .errorOut(
        oneOf(
          oneOfVariant[DeckError.GenericError](
            statusCode(InternalServerError)
              .and(jsonBody[DeckError.GenericError])
          ),
          oneOfVariant[DeckError.NotFoundError](
            statusCode(NotFound).and(jsonBody[DeckError.NotFoundError])
          )
        )
      )
