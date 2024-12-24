package br.com.flashcards.adapter.endpoint

import br.com.flashcards.adapter.endpoint.doc.DeckDocEndpoint
import br.com.flashcards.adapter.endpoint.request.{
  DeckInsertRequest,
  DeckUpdateRequest
}
import br.com.flashcards.adapter.endpoint.response.error.DeckError
import br.com.flashcards.adapter.endpoint.response.{
  DeckDetailsResponse,
  DeckInsertedResponse,
  DeckListResponse,
  DeckUpdatedResponse
}
import br.com.flashcards.core.exception.DeckException
import br.com.flashcards.core.service.query.DeckRead
import br.com.flashcards.core.service.{
  DeckWrite,
  InsertDeckDomain,
  UpdateDeckDomain
}
import io.scalaland.chimney.dsl.*
import sttp.tapir.ztapir.*
import zio.*

import java.time.OffsetDateTime

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
          _ => DeckError.GenericError("", "", 500, OffsetDateTime.now()),
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
          _ => DeckError.GenericError("", "", 500, OffsetDateTime.now()),
          _.into[DeckDetailsResponse].transform
        )

    DeckDocEndpoint.findByIdEndpoint.zServerLogic(p => findByIdLogic(p))

  private def insert(): ZServerEndpoint[Any, Any] =
    def insertLogic(
        request: DeckInsertRequest
    ) =
      write
        .insert(request.into[InsertDeckDomain].transform)
        .mapBoth(
          _ => DeckError.GenericError("", "", 500, OffsetDateTime.now()),
          _.into[DeckInsertedResponse].transform
        )

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
          _ => DeckError.GenericError("", "", 500, OffsetDateTime.now()),
          _.into[DeckUpdatedResponse].transform
        )

    DeckDocEndpoint.updateEndpoint.zServerLogic(p => updateLogic(p._1, p._2))

  private def delete(): ZServerEndpoint[Any, Any] =
    def deleteLogic(
        id: Long
    ) =
      write
        .delete(id)
        .orElseFail(DeckError.GenericError("", "", 500, OffsetDateTime.now()))

    DeckDocEndpoint.deleteEndpoint.zServerLogic(p => deleteLogic(p))

object DeckEndpoint:

  val layer: ZLayer[
    DeckWrite & DeckRead,
    DeckException,
    DeckEndpoint
  ] = ZLayer.fromFunction(DeckEndpoint(_, _))
