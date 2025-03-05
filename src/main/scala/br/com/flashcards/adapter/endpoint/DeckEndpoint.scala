package br.com.flashcards.adapter.endpoint

import br.com.flashcards.adapter.endpoint.doc.DeckDocEndpoint
import br.com.flashcards.adapter.endpoint.request.{
  DeckInsertRequest,
  DeckInsertRequestValidator,
  DeckUpdateRequest
}
import br.com.flashcards.adapter.endpoint.response.{
  DeckDetailsResponse,
  DeckInsertedResponse,
  DeckListResponse,
  DeckUpdatedResponse
}
import br.com.flashcards.core.exception.DeckServiceError
import br.com.flashcards.core.service.query.DeckRead
import br.com.flashcards.core.service.{
  DeckWrite,
  InsertDeckDomain,
  UpdateDeckDomain
}
import io.scalaland.chimney.dsl.*
import sttp.tapir.ztapir.*
import zio.*

case class DeckEndpoint(
    write: DeckWrite,
    read: DeckRead
):

  val endpoints: List[ZServerEndpoint[Any, Any]] =
    List(
      list(),
      findById(),
      insert(),
      update(),
      delete()
    )

  private def list(): ZServerEndpoint[Any, Any] =
    def listLogic() =
      read
        .list()
        .mapBoth(
          mapErrorResponse(_),
          d => d.map(_.into[DeckListResponse].transform)
        )

    DeckDocEndpoint.listEndpoint.zServerLogic(_ => listLogic())

  private def findById(): ZServerEndpoint[Any, Any] =
    def findByIdLogic(
        id: Long
    ) =
      read
        .findById(id)
        .mapBoth(
          mapErrorResponse(_),
          _.into[DeckDetailsResponse].transform
        )

    DeckDocEndpoint.findByIdEndpoint.zServerLogic(p => findByIdLogic(p))

  private def insert(): ZServerEndpoint[Any, Any] =
    def insertLogic(
        request: DeckInsertRequest
    ) =
      for {
        validated <- checkConstraints(request, DeckInsertRequestValidator)
        response <- write
          .insert(request.into[InsertDeckDomain].transform)
          .mapBoth(
            mapErrorResponse(_),
            _.into[DeckInsertedResponse].transform
          )
      } yield response

    DeckDocEndpoint.insertEndpoint.zServerLogic(p => insertLogic(p))

  private def update(): ZServerEndpoint[Any, Any] =
    def updateLogic(
        id: Long,
        request: DeckUpdateRequest
    ) =
      write
        .update(
          request.into[UpdateDeckDomain].withFieldConst(_.id, id).transform
        )
        .mapBoth(
          mapErrorResponse(_),
          _.into[DeckUpdatedResponse].transform
        )

    DeckDocEndpoint.updateEndpoint.zServerLogic(p => updateLogic(p._1, p._2))

  private def delete(): ZServerEndpoint[Any, Any] =
    def deleteLogic(
        id: Long
    ) =
      write
        .delete(id)
        .mapError(mapErrorResponse(_))

    DeckDocEndpoint.deleteEndpoint.zServerLogic(p => deleteLogic(p))

object DeckEndpoint:

  val layer: ZLayer[
    DeckWrite & DeckRead,
    DeckServiceError,
    DeckEndpoint
  ] = ZLayer.fromFunction(DeckEndpoint(_, _))
