package br.com.flashcards.core.service

import br.com.flashcards.adapter.outbound.persistence.exception.DeckRepositoryError
import br.com.flashcards.core.exception.{DeckServiceError, GenericError}
import br.com.flashcards.core.port.persistence.DeckWritePort
import br.com.flashcards.core.service.impl.DeckService
import br.com.flashcards.core.service.query.DeckRead
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock
import zio.*
import zio.test.Assertion.{equalTo, fails}
import zio.test.{Spec, ZIOSpecDefault, assertZIO}

object DeckWriteSpec extends ZIOSpecDefault {
  def spec: Spec[Any, DeckServiceError] = suite("DeckService Insert Tests")(
    test("when insert with success then return data") {
      val writePortMock = mock[DeckWritePort]
      val query = mock[DeckRead]
      val deckWrite = DeckService(writePortMock, query)

      val domain = InsertDeckDomain("sample", "sample", List(InsertCardDeckDomain("front", "back")))
      val inserted = InsertedDeckDomain(1, "sample", "sample", List(InsertedCardDeckDomain(1, "front", "back")))

      when(writePortMock.insert(domain)).thenReturn(ZIO.succeed(inserted))
      val actual = deckWrite.insert(domain)
      assertZIO(actual)(equalTo(inserted))
    },

    test("when insert with error then return error") {
      val writePortMock = mock[DeckWritePort]
      val query = mock[DeckRead]
      val deckWrite = DeckService(writePortMock, query)

      val domain = InsertDeckDomain("sample", "sample", List(InsertCardDeckDomain("front", "back")))
      val inserted = InsertedDeckDomain(1, "sample", "sample", List(InsertedCardDeckDomain(1, "front", "back")))

      when(writePortMock.insert(domain)).thenReturn(ZIO.fail(DeckRepositoryError("sample", "sample", List.empty)))
      val actual = deckWrite.insert(domain).debug("")
      assertZIO(actual.exit)(fails(equalTo(GenericError("sample", "sample", List.empty))))
    }
  )
}
